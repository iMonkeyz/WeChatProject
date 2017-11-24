var Page = {
	init: function () {
		this.removeInfo();
	},
	removeInfo: function () {
		$(".info-block").on("click", ".glyphicon-trash", function () {
			if ( confirm("信息被删除后将不可恢复, 请确认操作?") ) {
				$(this).parents(".info-block").animate({opacity: 0}, "1000", function () {
					$.get(`/wx/group/admin/remove/`, function (ok) {
						if ( ok ) {
							$(this).remove();
						} else {
							alert("删除失败!");
						}
					})
				});
			}
		});
	}
};

$(()=>{
	Page.init();
});