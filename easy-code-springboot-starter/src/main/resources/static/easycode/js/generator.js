$(function () {
    $("#jqGrid").jqGrid({
        url: '/generator/list',
        datatype: "json",
        colModel: [			
			{ label: '表名', name: 'tableName', width: 100, key: true },
			{ label: 'Engine', name: 'engine', width: 70},
			{ label: '表备注', name: 'tableComment', width: 100 },
			{ label: '创建时间', name: 'createTime', width: 100 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100,200],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			tableName: null,
            dataSource:''
		},
        dataSources:{}
	},
    mounted: function(){
        $.ajax({
            //几个参数需要注意一下
            type: "GET",//方法类型
            contentType: "application/json",
            dataType: "json",//预期服务器返回的数据类型
            url: "/generator/dataSources",
            success: function (result) {
                vm.dataSources = result.dataSources
            },
            error : function(result) {
                console.log(result)
                alert("异常！");
            }
        });
    },
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'tableName': vm.q.tableName,'dataSource':vm.q.dataSource},
                page:1 
            }).trigger("reloadGrid");
		},

        generatorToDirect:function(){
            var tableNames = getSelectedRows();
            if(tableNames == null){
                return ;
            }

            var datas = []
            for (var index in tableNames){
                datas.push(tableNames[index]);
            }
            console.log(tableNames)
            console.log(JSON.stringify(tableNames))
            console.log(datas)
            console.log(JSON.stringify(datas))
            var reqData = {
                "tableNames":datas,
                "dataSource": vm.q.dataSource
            }
            $.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                contentType: "application/json",
                dataType: "json",//预期服务器返回的数据类型
                data: JSON.stringify(reqData),
                url: "/generator/codeToDirect",
                success: function (result) {
                    alert("操作成功");
                },
                error : function(result) {
                    console.log(result)
                    alert("异常！");
                }
            });
        }
	}
});

