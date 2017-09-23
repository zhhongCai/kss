
layui.use(['jquery', 'element', 'table', 'layer', 'form'], function(){
    var element = layui.element;
    var $ = layui.$;
    var form = layui.form;
    var userTable = layui.table;

    userTable.render({
        id: 'userTableId',
        elem: '#userTable',
        url: '/user/tableData',
        page: true,
        method: 'post',
        cols:[[
            {checkbox: true},
            {field:'id', title:'ID', width: 150},
            {field:'username', title:'用户名', width: 200},
            {field:'code', title:'code', width: 200 },
            {field:'phone', title:'电话', width: 150},
            {field:'createTime', title:'创建时间', width: 150}
        ]],
        done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            console.log(res);

            //得到当前页码
            console.log(curr);

            //得到数据总量
            console.log(count);
        }
    });

    var initForm = function(data) {
        $('#userForm').find('input[name="id"]').val(data.id);
        $('#userForm').find('input[name="username"]').val(data.username);
        $('#userForm').find('input[name="code"]').val(data.code);
        $('#userForm').find('input[name="phone"]').val(data.phone);
    };

    var active = {
        addUser: function(){
            layer.open({
                type: 1,
                title: '新增用户',
                maxWidth: 450,
                content: $('#userForm')
            });
            initForm({
                id: '',
                username: '',
                code: '',
                phone: ''
            });
        },
        editUser: function(){
            var checkStatus = userTable.checkStatus('userTableId'),data = checkStatus.data;

            if(!data || data.length != 1) {
                layer.alert("请选择一条记录");
                return;
            }
            layer.open({
                type: 1,
                title: '修改用户',
                maxWidth: 450,
                content: $('#userForm')
            });
            initForm(data[0]);
        },
        deleteUser: function(){ //验证是否全选
            var checkStatus = userTable.checkStatus('userTableId'),data = checkStatus.data;
            if(!data || data.length == 0) {
                layer.alert("请选择要删除的记录");
                return;
            }
            layer.confirm('确定要删除选中的记录?', {icon: 3, title:'确认'}, function(index){
                var ids = [];
                $.each(data, function(index, d) {
                    ids.push(d.id);
                });
                $.ajax({
                    url: '/user/delete',
                    data: {ids : ids.join(',')},
                    type: 'post',
                    dataType: 'json',
                    success: function (result) {
                        if(result.code == '0') {
                            layer.alert("删除成功！");
                            window.location.reload();
                        } else {
                            layer.alert("删除失败：" + result.msg);
                        }
                    }
                });

                layer.close(index);
            });
        },
        searchUser: function() {
            var username = $('#username').val();
            if(!!username) {
                username = '%' + $('#username').val() + '%';
            }
            userTable.reload('userTableId', {
                where: {
                    username: username
                }
            });
        }
    };

    //按钮事件
    $('.layui-body .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //表单提交
    form.on('submit(saveBtn)', function(data){
        $.ajax({
            url: '/user/saveOrUpdate',
            data: data.field,
            type: 'post',
            dataType: 'json',
            success: function (result) {
                if(result.code == '0') {
                    layer.alert("保存成功！");
                    window.location.reload();
                } else {
                    layer.alert("保存失败：" + result.msg);
                }
            }
        });
        return false;
    });
});
