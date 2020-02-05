//初始化数据
function init(){
	window.gradeIds = [];
	window.gradeNames = [];
	window.quesIds = []; //存储字符串数组，用于识别重复值
	window.questions = []; //存储json数组
};
//渲染grid
layui.use(['table'], function(){
	var table = layui.table;

	table.render({
		elem: '#grid',
		id: 'grid',
		url: $.kbase.ctx + '/exam/paper/loadList',
		cellMinWidth: 80,
		cols: [[
		    {type:'checkbox'},
		    {type:'numbers'},
	        {field:'name', title: '试卷名称', sort: true},
	        {field:'gradeName', title: '班级', width: 120, sort: true, templet: function(data){
	        	var desc = [];
	        	var grades = data.grades;
	        	if (grades!=null){
	        		$(grades).each(function(index, item){
	        			if (item){
	        				desc.push(item.name);
	        			}
	        		});
	        	}
	        	return desc.join(', ');
	        }},
	        {field:'total', title: '总分', width: 80, sort: true},
	        {field:'limit', title: '限时', width: 80, sort: true},
	        {title: '有效期', width: 220, templet: function(data){
	        	var startDate = data.startDate!=null?dateFns.format(data.startDate, 'YYYY-MM-DD'):'';
	        	var endDate = data.endDate!=null?dateFns.format(data.endDate, 'YYYY-MM-DD'):'';
	        	return startDate + ' ~ ' + endDate;
	        }, sort: true},
	        {field:'createUser', title: '创建人', width: 120, sort: true},
	        {field:'delFlag', title: '状态', width: 80, templet: function(data){
	        	return data.delFlag==0?'启用':'停用';
	        }, sort: true}
	    ]],
	    page: true
	});
});

//新增和编辑
$('#btnAdd, #btnEdit').click(function(){
	init();
	var _this = this;
	layui.use(['layer', 'table', 'laydate', 'form'], function(){
		var layer = layui.layer;
		var table = layui.table;
		var laydate = layui.laydate;
        var form = layui.form;
		
		if (_this.id=='btnEdit'){
			//编辑
			var checked = table.checkStatus('grid');
			if (checked.data.length>0){
				var row = checked.data[0];
				
				//console.log(row);
				
				$('#id').val(row.id);
				$('input[name="name"]').val(row.name);
				$('input[name="limit"]').val(row.limit);
				$('input[name="total"]').val(row.total);
				// 渲染是否立即显示答案
                console.log(row.displayTofAtReply);
                $('input[name="displayTofAtReply"][value="' + row.displayTofAtReply + '"]').attr('checked', 'checked');

                //渲染班级
				if (row.grades!=null && row.grades.length>0){
					$(row.grades).each(function(i, item){
						if (item){
							gradeIds.push(item.id);
							gradeNames.push(item.name);
						}
					});
					$('.zx-grade-panel').html(gradeNames.join(' '));
				}
				//问答渲染
				//if (row.questionList!=null && row.questionList.length>0){
				$.get($.kbase.ctx + '/exam/paper/sort?id=' + row.id, function(row){
					$(row.questionList).each(function(i, question){
						for (var key in question){
							var item = question[key];
							questions.push({
								id: item.id,
								name: item.name,
								type: item.type,
								score: item.score,
								order: item.order
							});
						}
					});
					$('#quesPanel').html(template('templateQues', questions));
				});
					
				//}
				var date = new Date();
				if (row.startDate!=null){
					laydate.render({elem: '#startDate', value: dateFns.format(row.startDate, 'YYYY-MM-DD')});
				}else{
					laydate.render({elem: '#startDate', value: dateFns.format(date, 'YYYY-MM-DD')});
				}
				date.setTime(date.getTime() + 31*24*60*60*1000);
				if (row.endDate!=null){
					laydate.render({elem: '#endDate', value: dateFns.format(row.endDate, 'YYYY-MM-DD')});
				}else{
					laydate.render({elem: '#endDate', value: dateFns.format(date, 'YYYY-MM-DD')});
				}

				form.render();
			}else{
				return false;
			}
		}else{
			//新增
			init();
			$('#id').val('');
			$('input[name="name"]').val('');
			$('.zx-grade-panel').empty();
			$('#quesPanel').empty();
			//有效期默认一个月
			var date = new Date();
			laydate.render({elem: '#startDate', value: dateFns.format(date, 'YYYY-MM-DD')});
			date.setTime(date.getTime() + 31*24*60*60*1000);
			laydate.render({elem: '#endDate', value: dateFns.format(date, 'YYYY-MM-DD')});
		}
		
		layer.open({
			type: 1,
			btn: ['保存'],
			area: ['auto', 'auto'],
			content: $('.zx-panel'),
			yes: function(index, layero){
				
				if ($('input[name="name"]').val().trim()==''){
					$('input[name="name"]').focus();
					return false;
				}
				if (gradeIds.length==0){
					layer.alert('请选择班级');
					return false;
				}
				
				//处理题目数据
				var _total = 0;
				var _arr = [];
				var _continue = true;
				$('#quesPanel tr').each(function(i, item){
					var json = {
						id: $(item).attr('_id'),
						score: $(item).find('input[name="score"]').val(),
						order: $(item).find('input[name="order"]').val()
					}
					if (json.score==''){
						$(item).find('input[name="score"]').focus();
						_continue = false;
						return false;
					}
					_arr.push(json);
					_total += Number(json.score);
				});
				
				if (!_continue) return false;
				
				if (_total!=Number($('input[name="total"]').val())){
					layer.alert('总分不匹配，请仔细检查分值');
					return false;
				}
				var param = {
					id: $('#id').val(),
					name: $('input[name="name"]').val(),
					limit: $('input[name="limit"]').val(),
					total: $('input[name="total"]').val(),
					startDate: $('input[name="startDate"]').val(),
					endDate: $('input[name="endDate"]').val(),
					gradeIds: gradeIds,
					questions: JSON.stringify(_arr),
                    displayTofAtReply: $('input[name="displayTofAtReply"]:checked').val()
				};
								
				$.post($.kbase.ctx + '/exam/paper/save', param, function(data){
					if (data.success){
						layer.alert('保存成功', function(index0){
							table.reload('grid', {page: {curr: 1}});
							layer.close(index0);
							layer.close(index);
						});
						
					}
				});
				
			}
		});
	});
});
//试卷启用或停用
$('#btnEnable').click(function(){
	layui.use(['layer', 'table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		var checked = table.checkStatus('grid');
		if (checked.data.length>0){
			var row = checked.data[0];
			var desc = row.delFlag==0?'停用':'启用';
			layer.confirm('确定' + desc + '吗', function(index){
				var param = {
					id: row.id
				}
				$.post($.kbase.ctx + '/exam/paper/enable', param, function(data){
					if (data.success){
						table.reload('grid', {page: {curr: 1}});
					}else{
						layer.alert(data.response.message);
					}
					layer.close(index);
				}, 'json');
			});
		}
	});
});
//删除
$('#btnDel').click(function(){
	layui.use(['layer', 'table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		
		var checked = table.checkStatus('grid');
		if (checked.data.length>0){
			layer.confirm('删除试卷会相应的删除所有考试记录，无法恢复，确定删除吗', function(index){
				var row = checked.data[0];
				var param = {
					id: row.id
				}
				$.post($.kbase.ctx + '/exam/paper/delete', param, function(data){
					if (data.success){
						table.reload('grid', {page: {curr: 1}});
					}else{
						layer.alert(data.response.message);
					}
					layer.close(index);
				}, 'json');
			});
		}
	});
});
//试卷复制
$('#btnCopy').click(function(){
	layui.use(['layer', 'table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		
		var checked = table.checkStatus('grid');
		if (checked.data.length==0) return false;
		
		layer.confirm('确定复制吗', function(index){
			if (checked.data.length>0){
				var row = checked.data[0];
				var param = {
					id: row.id
				}
				$.post($.kbase.ctx + '/exam/paper/copy', param, function(data){
					if (data.success){
						table.reload('grid', {page: {curr: 1}});
					}else{
						layer.alert(data.response.message);
					}
				}, 'json');
			}else{
				return false;
			}
			layer.close(index);
		});
	});
	
});
//查询
$('#btnQuery').click(function(){
	var keyword = $('#keyword').val();
//	if (keyword=='') {
//		$('#keyword').select();
//		return false;
//	}
	
	layui.use(['table'], function(){
		var table = layui.table;
		table.reload('grid', {
			page: {curr: 1},
			where: {
				keyword: keyword
			}
		});
	});
});
$('#keyword').keyup(function(e){
	if (e.keyCode==13){
		$('#btnQuery').click();
	}
});
//查阅成绩单
$('#btnView').click(function(){
	layui.use(['layer', 'table'], function(){
		var layer = layui.layer;
		var table = layui.table;
		var checked = table.checkStatus('grid');
		if (checked.data.length>0){
			var row = checked.data[0];
			table.render({
				elem: '#taskGrid',
				id: 'taskGrid',
				url: $.kbase.ctx + '/exam/paper/loadTask',
				cellMinWidth: 80,
				cols: [[
				    {type:'numbers'},
			        {field:'createUser', title: '学员', width: 120, sort: true},
			        {field:'score', title: '得分', width: 80, sort: true},
			        {field:'createDate', title: '开始时间', width: 160, sort: true, templet: function(data){
			        	var createDate = data.createDate!=null?dateFns.format(data.createDate, 'YYYY-MM-DD HH:mm'):'';
			        	return createDate;
			        }},
			        {field:'modifyDate', title: '完成时间', width: 160, sort: true, templet: function(data){
			        	var modifyDate = data.modifyDate!=null?dateFns.format(data.modifyDate, 'YYYY-MM-DD HH:mm'):'';
			        	return modifyDate;
			        }},
			        {field:'status', title: '状态', width: 120, sort: true, templet: function(data){
			        	return data.status==1?'已完成':'考试中';
			        }}
			    ]],
			    width: 700,
			    height: 260,
			    where: {
			    	paperId: row.id
			    }
			});
			layer.open({
				type: 1,
				btn: ['分析', '导出', '关闭'],
				area: ['720px', '380px'],
				content: $('.layui-table-view:eq(1)'),
				yes: function(index, layero){
					//alert('开发中，敬请期待');
					parent.addTab({
						id: 'taskResult', 
						url: $.kbase.ctx + '/exam/analysis/view?paperId=' + row.id,
						title: '考试结果分析'
					});
				},
				btn2: function (index, layero) {
					window.open($.kbase.ctx + '/exam/paper/exportTask?paperId=' + row.id);
                }
			});
		}
	});
});
//选择班级
$('#btnPick').click(function(){
	layui.use(['layer'], function(){
		var layer = layui.layer;
		layer.open({
			type: 2,
			btn: ['保存'],
			area: ['1000px', '460px'],
			content: $.kbase.ctx + '/exam/grade/list?_p=paper',
			yes: function(index, layero){
				var _win = $(layero).find("iframe")[0].contentWindow;
				//获取用户grid里选中的数据
				var checked = _win.laytable.checkStatus('grid');
				if (checked.data.length>0){
					$(checked.data).each(function(i, item){
						if ($.inArray(item.id, gradeIds)==-1){
							gradeIds.push(item.id);
							gradeNames.push(item.name);
						}
					});
					$('.zx-grade-panel').html(gradeNames.join(' '));
				}else{
					return false;
				}
				layer.close(index);
			}
		});
	});
});
//清空班级
$('#btnClear').click(function(){
	gradeIds = [];
	gradeNames = [];
	$('.zx-grade-panel').empty();
});

//选择试题
$('#btnPickQues').click(function(){
	layui.use(['layer'], function(){
		var layer = layui.layer;
		layer.open({
			type: 2,
			btn: ['保存'],
			area: ['1000px', '460px'],
			content: $.kbase.ctx + '/exam/question/view?_p=paper',
			yes: function(index, layero){
				var _win = $(layero).find("iframe")[0].contentWindow;
				//获取用户grid里选中的数据
				var checked = _win.laytable.checkStatus('grid');
				if (checked.data.length>0){
					var arr = [];
					var currIndex = $('#quesPanel tr').length;
					$(checked.data).each(function(i, item){
						if ($.inArray(item.id, quesIds)==-1){
							currIndex++;
							quesIds.push(item.id);
							questions.push({
								id: item.id,
								name: item.name,
								type: item.type,
								score: '',
								order: currIndex
							});
							arr.push({
								id: item.id,
								name: item.name,
								type: item.type,
								score: '',
								order: currIndex
							});
						}
					});
					$('#quesPanel').append(template('templateQues', arr));
				}else{
					return false;
				}
				layer.close(index);
			}
		});
	});
});

//数值校验
$('.zx-panel').on('blur', 'input[name="limit"], input[name="total"], input[name="score"]', function(){
	var re = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
	var _this = this;
	var _val = $(this).val().trim();
	if (_val!='' && !re.test(_val)){
		$(_this).focus();
		$(_this).select();
	}
});
//分数累加
$('.zx-panel').on('blur', 'input[name="score"]', function(){
	var _scoreTotal = 0;
	$('.zx-panel input[name="score"]').each(function(i, item){
		if ($(item).val()!='' && !isNaN(Number($(item).val()))){
			_scoreTotal += Number($(item).val());
		}
	});
	layui.use(['layer'], function(){
		var layer = layui.layer;
		layer.msg(_scoreTotal, {
		  offset: 't',
		  anim: 0
		});
	});
});
//单条试题删除，还需要删除 quesIds 和 questions 中的数据
$('.zx-panel').on('click', '.zx-ques-del', function(){
	var id = $(this).attr('_id');
	/*quesIds.push(item.id);
	questions.push({
		id: item.id,
		name: item.name,
		score: '',
		order: currIndex
	});*/
	var tmpQuesIds = [];
	$(quesIds).each(function(i, item){
		if (item!=id){
			tmpQuesIds.push(item);
		}
	});
	quesIds = tmpQuesIds;
	var tmpQuestions = [];
	$(questions).each(function(i, item){
		if (item.id!=id){
			tmpQuestions.push(item);
		}
	});
	questions = tmpQuestions;
	
	$(this).parents('tr').remove();
});
