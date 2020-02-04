window.AppExec = {
	pageInit: function (event, page) {
		//初始化沙漏
		/*public */function sandglass(data, forceSubmit){
			//倒计时
			var limit = data.limit || 120*60; //120分钟 limit单位是秒
			var t = limit; //秒数
			formatSandglass(t);
			window.__sandglass = window.setInterval(function(){
				formatSandglass(t--);
				window.tmpLimit = t;
				//如果倒计时少于10分钟，则给出提示
				if (t==10*60){
					toastLimit.open();
				}
				//如果倒计时少于1分钟，则给出提示
				if (t==1*60){
					toastLimitLast.open();
				}
				//倒计时结束，强制交卷
				if (t==0){
					if (forceSubmit && typeof(forceSubmit)=='function') forceSubmit();
					window.clearInterval(window.__sandglass);
				}
			}, 1*1000);

			/*private */function formatSandglass(t){
				var d = Math.floor(t/60/60/24);
				var h = Math.floor(t/60/60%24);
				var m = Math.floor(t/60%60);
				var s = Math.floor(t%60);
				$$('.zx-sandglass').text(formatHms(h) + ':' + formatHms(m) + ':' + formatHms(s));
			}
			//格式化时分秒
			/*private */function formatHms(h){
				if (h<10) return '0'+ h;
				return h;
			}
		}

		//var url = this.url; //"/task/5a5325b0c2941928e005f3de/5a6304dec294192d68a17ee4/0/"
		var paperId = event.detail.route.params.id;
		var quesId = '';
		//考试页面加载完毕后，对页面数据进行初始化 
		//axios 第二个参数有坑，用的时候当心哦，技术尝试 application/x-www-form-urlencoded 与 application/json 的区别
		axios.post(__ctx + '/exam/exec/loadPaper', 'id=' + paperId)
		.then(function (response) {
			console.log(response.data);
			/* 初始化倒计时 */
			sandglass(response.data, function(){
				$$('.zx-submit').click();
			});
			var paper = response.data.paper;
			$$('.zx-paper-title').text(paper.name);
			var questions = response.data.questionList;
			var i=0;
//			var cardsCompile = Template7.compile($$('#templetCards').html());
//			$$('.zx-cards').html(cardsCompile(response.data));
			//渲染答题卡，注意： $$(questions).each(callback) 无法使用
			for (var i=0;i<questions.length;i++){
				var question = questions[i];
				for (var key in question){ //只循环一次
					var item = question[key];
					var _ind = i+1;
					if (_ind<10) _ind='0'+_ind;
					primaryClass = item.isReply?'color-black':'';
					$$('.zx-cards').append('<button class="button zx-btn ' + primaryClass + '" _id="' + item.id +'" _score="' + item.score + '" _reply="' + item.isReply + '">' + _ind + '</button>');
				}
			}

			//页面加载完毕后单击第一个试题
			$$('.zx-cards').find('.zx-btn').eq(0).click();
		})
		.catch(function (error) {
			console.log(error);
		});

		var singleAnsCompile = Template7.compile($$('#templetSingle').html());
		var multiAnsCompile = Template7.compile($$('#templetMulti').html());
		var tofAnsCompile = Template7.compile($$('#templetTof').html());

		//监听答题卡单击事件
		$$('.zx-cards').on('click', '.button', function(){
			var _this = this;
			quesId = $$(_this).attr('_id'); //问题id
			//
			$$('.zx-cards').find('.zx-btn').removeClass('color-red');
			$$(_this).addClass('color-red');

			axios.post(__ctx + '/exam/exec/loadQues', 'id=' + quesId + '&paperId=' + paperId)
			.then(function (response) {
				var curr = $$('.zx-cards').find('.color-red').index();
				var data = response.data;
				var typeDesc = data.type==0?'单选题':(data.type==1?'多选题':'判断题');
				var typeColor = data.type==0?'color-green':(data.type==1?'color-pink':'color-orange');
				$$('.zx-ques').html('<span class="badge ' + typeColor + '">' + typeDesc + '</span> ' + (curr+1) + '. ' + data.name + '(' + data.score + ' 分)');
				$$('.zx-ques').attr('_type', data.type);

				if (data.type==0){
					console.log(data.answers);
					$$('.zx-ans').html(singleAnsCompile(data));
					if (data.isReply){
						for (var i=0;i<data.answers.length;i++){
							var item = data.answers[i];
							if (item.mark){
								$$('input[name="ans"][value="' + item.id + '"]').attr('checked', 'checked');
							}
						}
					}
				}else if (data.type==1){
					$$('.zx-ans').html(multiAnsCompile(data));
					if (data.isReply){
						for (var i=0;i<data.answers.length;i++){
							var item = data.answers[i];
							if (item.mark){
								$$('input[name="ans"][value="' + item.id + '"]').attr('checked', 'checked');
							}
						}
					}
				}else if (data.type==2){
					$$('.zx-ans').html(tofAnsCompile({}));
					if (data.isReply){
						$$('input[name="ans"][value="' + data.keyMark + '"]').attr('checked', 'checked');
					}
				}

			})
			.catch(function (error) {
				console.log(error);
			});
		});
		//监听上一题
		$$('.zx-prev').on('click', function(){
			var curr = $$('.zx-cards').find('.color-red').index();
			if (curr==0) return false;
			curr--;
			$$('.zx-cards').find('.zx-btn').eq(curr).click();
		});
		//监听下一题
		$$('.zx-next').on('click', function(){
			var len = $$('.zx-cards .zx-btn').length;
			var curr = $$('.zx-cards').find('.color-red').index();
			curr++;
			if (curr==len) return false;
			$$('.zx-cards').find('.zx-btn').eq(curr).click();
		});
		//监听答案变化
		$$('.zx-ans').on('click', '.item-content', function(){
			var _this = this;
			//如果没有timeout 会出现一个问题是无法拿到当前选中的数据
			window.setTimeout(function(){
				var ans = [];
				$$('input[name="ans"]:checked').each(function(i, item){
					ans.push($$(item).val());
				});
				var param = {
						paperId: paperId,
						quesId: quesId,
						ans: ans.join(','),
						limit: window.tmpLimit
				};
				app.request.postJSON(__ctx + '/exam/exec/save', param, function(data){
					$$('.zx-cards').find('.color-red').addClass('color-black');
				});
			}, 5);
		});
		//交卷
		$$('.zx-submit').click(function(){
			//校验用户是否有未回答的试题
			if ($$('.zx-cards').find('.color-black').length!=$$('.zx-cards').find('.button').length){
				app.toast.create({
					text: '尚有未完成的试题，请完成后交卷'
				}).open();
				return false;
			}

			//试题全部完成，则确认是否提交
			app.dialog.confirm('确定提交吗', function(index){
				var params = {
						id: paperId
				};
				app.request.postJSON(__ctx + '/exam/exec/submit', params, function(data){
					var taskId = data.id;
					var score = data.score;
					app.dialog.alert('提交成功，本次考试成绩为 ' + score + ' 分', function(){
						// location.href = __ctx + '/exam/task/read?taskId=' + taskId;
						location.reload();
                        // app.router.navigate('/task/0/' + taskId + '/1/');
                        // app.router.back(__ctx + '/#tab-task', {force: true, ignoreCache: true})
					});
				});
			});
		});
		$$('.zx-read-btn').on('click', function(){
		    // alert(11)
            // app.router.navigate({name: 'task'})
            // app.router.back('/#tab-task', {ignoreCache: true})
            // app.router.back(__ctx + '/', {force: true})
        })

	},
	pageAfterOut: function(event, page) {
		window.clearInterval(window.__sandglass);
	}
};