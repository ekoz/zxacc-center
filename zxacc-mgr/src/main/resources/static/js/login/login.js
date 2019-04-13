$('input[name="username"]').focus();
$('.layui-form input').keyup(function(e){
    if (e.keyCode==13){
        $('#btnSubmit').click();
    }
});
layui.use('layer', function() { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
    $('#btnSubmit').click(function(){
        if ($('input[name="username"]').val().trim()==''){
            $('input[name="username"]').focus();
            return false;
        }
        if ($('input[name="password"]').val().trim()==''){
            $('input[name="password"]').focus();
            return false;
        }
        $.post($.kbase.ctx + '/login', {
            username: $('input[name="username"]').val(),
            password: $('input[name="password"]').val()
        }, function(data){
            if (data.type==1){
                location.href = 'main';
            }else{
                layer.msg('账号和密码不正确，请重新输入');
            }
        }, 'json');
        return false;
    });
});