var Page = {
	expireTimer: null,
	expiredMs: 1000 * 60 * 2,
	validationTimer: null,
	init: function () {
		this.loadQr();
		this.refreshQr();
	},
	loadQr: function () {

		/*
		$.get(`${location.href}/qr/auth?${new Date().getTime()}`, (res) => {
			Page.uuid = res.uuid;
			$(".login_box .qrcode .img").attr("src", `data:image/png;base64,${res.data}`);
		});
		*/
		$(".login_box .qrcode .img").attr("src", `${location.href}/qr/auth?${new Date().getTime()}`);
		$(".login_box").removeClass("expired");

		//超时倒计时
		clearTimeout(this.expireTimer);
		this.expireTimer = setTimeout(() => {
			$(".login_box").addClass("expired");
			clearInterval(this.validationTimer);
		}, this.expiredMs);

		//监听请求
		clearInterval(this.validationTimer);
		this.validationTimer = setInterval(() => {
			$.get(`${location.href}/validation?${new Date().getTime()}`, (res) => {
				if ( res == "200" ) {
					location.reload();
				}
			})
		}, 3000);
		/*
		clearInterval(this.validationTimer);
		this.validationTimer = setInterval(() => {
			$.get(`${location.href}/validation/${this.uuid}?${new Date().getTime()}`, (res) => {
				if ( res == "200" ) {
					location.reload();
				}
				if ( res == "999" ) {
					Page.loadQr();
				}
			})
		}, 3000);
		*/

	},
	refreshQr: function () {
		$(".login_box").on("click", ".icon-refresh", function () {
			$(this).addClass("rotateLoading");
			setTimeout(()=>{
				$(this).removeClass("rotateLoading");
				Page.loadQr();
			}, 800);
		})
	}
};

$(()=>{
	Page.init();
});