window.AppRead = {
	pageInit: function (event, page) {
		//var url = this.url; //"/task/5a5325b0c2941928e005f3de/5a6304dec294192d68a17ee4/0/"
		var taskId = event.detail.route.params.taskid;
		
		var faqCompile = Template7.compile($$('#templetFaq').html());
		var singleAnsCompile = Template7.compile($$('#templetSingle').html());
		var multiAnsCompile = Template7.compile($$('#templetMulti').html());
		var tofAnsCompile = Template7.compile($$('#templetTof').html());
		
		//考试页面加载完毕后，对页面数据进行初始化 
		//axios 第二个参数有坑，用的时候当心哦，技术尝试 application/x-www-form-urlencoded 与 application/json 的区别
		axios.post(__ctx + '/exam/task/loadTask', {taskId: taskId})
		.then(function (response) {
			console.log(response.data);
			var task = response.data;
			$$('.zx-paper-title').text(task.paper.name);
			$$('.zx-total').text(task.score);
			var questions = task.questionList;
			//渲染答题卡，注意： $$(questions).each(callback) 无法使用
			if (questions!=null){
                for (var i=0;i<questions.length;i++){
                    var question = questions[i];
                    for (var key in question){
                        var item = task.questions[key];
                        var quesName = (i+1) + "、" + item.name;
                        if (item.type==0){
                            //单选题
                            var content = singleAnsCompile(item);
                            $$('#panel').append(faqCompile({id: item.id, quesName: quesName, score: item.score, content: content}));
                        }else if (item.type==1){
                            //多选题
                            var content = multiAnsCompile(item);
                            $$('#panel').append(faqCompile({quesName: quesName, score: item.score, content: content}));
                        }else if (item.type==2){
                            //判断题
                            var content = tofAnsCompile(item);
                            $$('#panel').append(faqCompile({quesName: quesName, score: item.score, content: content}));
                        }
                    }
                }
            }
		})
		.catch(function (error) {
			console.log(error);
		});
	},
	pageAfterOut: function(event, page) {
		
	}
};