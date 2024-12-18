window.onload = function() {
	// ローカルストレージからURLを取得して表示
	const storedUrl = localStorage.getItem("calendarUrl");
	console.log("Stored URL:", storedUrl); // ログ出力して確認
	if (storedUrl) {
		displayCalendar(storedUrl);
	} else {
		console.log("No stored URL found");
	}

	// デフォルトで1時間後の終了時間を設定
	document.getElementById("duration").value = "60";

	if (document.getElementById("startTime").value) {
		calculateEndTime();
	}
};

// カレンダーの埋め込みコードからURLを抽出して表示
function embedCalendar() {
	const embedCode = document.getElementById("calendarEmbedCode").value;

	// `<iframe src="` と `"></iframe>`を取り除いてURL部分だけを抽出
	const urlMatch = embedCode.match(/<iframe src="([^"]+)"/);
	const url = urlMatch ? urlMatch[1] : null;

	if (url) {
		// ローカルストレージに保存
		localStorage.setItem("calendarUrl", url);
		console.log("Saving URL:", url); // ログ出力して保存内容確認

		// カレンダー表示
		displayCalendar(url);
	} else {
		alert("無効な埋め込みコードです。正しいURLを入力してください。");
	}
}

function removeCalendar() {
	// iframeを非表示にし、srcをリセット
	const iframe = document.getElementById("calendarIframe");
	iframe.src = ""; // srcをクリア
	iframe.style.display = "none"; // iframeを非表示

	// フォームの入力内容をクリア
	document.getElementById("calendarEmbedCode").value = "";
	
	// ローカルストレージからURLを削除
	localStorage.removeItem("calendarUrl");

	alert("同期を解除しました。");
}

// カレンダーを表示する
function displayCalendar(url) {
	const iframe = document.getElementById("calendarIframe");

	// iframeのsrc属性にURLをセット
	iframe.src = url;
	iframe.style.display = "block";  // iframeを表示
}













$('#updateCompanyModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	var id = button.data('id');
	var name = button.data('name');
	var contact = button.data('contact');
	var email = button.data('email');
	var phone = button.data('phone');
	var website = button.data('website');

	var modal = $(this);
	modal.find('#updateCompanyId').val(id);
	modal.find('#updateName').val(name);
	modal.find('#updateContactPerson').val(contact);
	modal.find('#updateEmail').val(email);
	modal.find('#updatePhone').val(phone);
	modal.find('#updateWebsite').val(website);
});

// 企業削除モーダルにデータを設定
$('#deleteModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	var id = button.data('id');
	var modal = $(this);
	modal.find('#deleteCompanyId').val(id);
});

// 面接情報追加モーダルに会社IDを設定
$('#addInterviewModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	var companyId = button.data('company-id');
	var modal = $(this);

	modal.find('#addInterviewCompanyId').val(companyId);
	var form = modal.find('#addInterviewForm');
	var actionUrl = form.attr('action').replace('{ID}', companyId);
	form.attr('action', actionUrl);
});

// 開始時間または所要時間が変更された際に終了時間を再計算する関数
function calculateEndTime() {
	const startTime = document.getElementById("startTime").value;
	const duration = parseInt(document.getElementById("duration").value, 10);

	if (startTime && !isNaN(duration)) {
		let [startHours, startMinutes] = startTime.split(':').map(Number);

		// 所要時間を追加
		startMinutes += duration;
		startHours += Math.floor(startMinutes / 60);
		startMinutes %= 60;

		// 終了時間を設定
		const endTime = document.getElementById("endTime");
		endTime.value = `${String(startHours).padStart(2, '0')}:${String(startMinutes).padStart(2, '0')}`;
	}
}

// 面接情報更新モーダルにデータを設定
$('#updateInterviewModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	var companyId = button.data('company-id');
	var id = button.data('id');
	var date = button.data('date');
	var start = button.data('start');
	var end = button.data('end');
	var location = button.data('location');
	var job = button.data('job');
	var status = button.data('status');
	var motivation = button.data('motivation');

	var modal = $(this);
	modal.find('#updateInterviewId').val(id);
	modal.find('#updateInterviewDate').val(date);
	modal.find('#updateStartTime').val(start);
	modal.find('#updateEndTime').val(end);
	modal.find('#updateLocation').val(location);
	modal.find('#updateJobTitle').val(job);
	modal.find('#updateSelectionStatus').val(status);
	modal.find('#updateMotivation').val(motivation);

	var form = modal.find('#updateInterviewForm');
	var actionUrl = form.attr('action').replace('{interviewId}', id);
	form.attr('action', actionUrl);
});

// 更新用モーダルの終了時間を計算する関数
function calculateUpdateEndTime() {
	const startTime = document.getElementById("updateStartTime").value;
	const duration = parseInt(document.getElementById("updateDuration").value, 10);

	if (startTime && !isNaN(duration)) {
		let [startHours, startMinutes] = startTime.split(':').map(Number);

		// 所要時間を追加
		startMinutes += duration;
		startHours += Math.floor(startMinutes / 60);
		startMinutes %= 60;

		// 終了時間を設定
		const endTime = document.getElementById("updateEndTime");
		endTime.value = `${String(startHours).padStart(2, '0')}:${String(startMinutes).padStart(2, '0')}`;
	}
}

// モーダルが表示される際にデフォルトで1時間後の終了時間を設定する
$('#updateInterviewModal').on('show.bs.modal', function(event) {
	// 所要時間を1時間に設定
	document.getElementById("updateDuration").value = "60";

	// 開始時間がすでに入力されていれば、終了時間も再計算
	if (document.getElementById("updateStartTime").value) {
		calculateUpdateEndTime();
	}
});




// 面接情報削除モーダルにデータを設定
$('#deleteInterviewModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	var id = button.data('id');
	var modal = $(this);
	modal.find('#deleteInterviewId').val(id);

	var form = modal.find('#deleteInterviewForm');
	var actionUrl = form.attr('action').replace('{interviewId}', id);
	form.attr('action', actionUrl);
});

function showTemplateSelector(companyName, contactPerson, email) {
	window.selectedCompanyName = companyName;
	window.selectedContactPerson = contactPerson;
	window.selectedEmail = email;
	$('#templateSelector').modal('show');
}

function closeTemplateSelector() {
	$('#templateSelector').modal('hide');
}

$('#templateSelector').on('hidden.bs.modal', function() {
	// 追加処理
});

function createEmail(templateType) {
	let subject = "";
	let body = "";
	const contactPerson = window.selectedContactPerson || "採用担当者"; // 担当者情報がない場合は「採用担当者」

	if (templateType === '書類送付') {
		subject = "書類送付のお知らせ";
		body = `${window.selectedCompanyName} ${contactPerson}様\nお世話になっております。\n\n以下の書類を送付いたしますので、ご確認ください。\n\n- [履歴書]\n- [職務経歴書]\n\nご不明な点がございましたら、お気軽にお知らせください。\n\n何卒よろしくお願いいたします。\n\n坂口  拓`;
	} else if (templateType === '日程調整') {
		subject = "日程調整のお願い";
		body = `${window.selectedCompanyName} ${contactPerson}様\nお世話になっております。\n\n面談の日程について、以下の候補をご提案いたします。\n\n- [候補日1]\n- [候補日2]\n- [候補日3]\n\nご都合はいかがでしょうか？お手数ですが、確認いただければ幸いです。\n\n何卒よろしくお願いいたします。\n\n坂口  拓`;
	} else if (templateType === '御礼') {
		subject = "御礼のメール";
		body = `${window.selectedCompanyName} ${contactPerson}様\nお世話になっております。\n\n先日はお忙しい中お時間をいただき、誠にありがとうございました。\nおかげさまで大変有意義な時間を過ごすことができました。\n\n引き続きよろしくお願いいたします。\n\n何かご不明な点があれば、いつでもお知らせください。\n\n坂口  拓`;
	}

	const mailtoLink = `https://mail.google.com/mail/?view=cm&fs=1&to=${window.selectedEmail}&su=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;
	window.open(mailtoLink, '_blank');
	closeTemplateSelector();
}

function toggleSidebar() {
	const sidebar = document.getElementById('sidebar');
	const mainContent = document.getElementById('main-content');

	sidebar.classList.toggle('open'); // サイドバーを開閉
	mainContent.classList.toggle('shifted'); // メインコンテンツの位置を調整
}

function addInterviewToCalendar(event) {
	// イベントの発生元の行を取得
	const interviewRow = event.target.closest("tr");

	// 各セルから面接情報を取得
	const date = interviewRow.children[0].textContent.trim();
	const startTime = interviewRow.children[1].textContent.trim();
	const endTime = interviewRow.children[2].textContent.trim();
	const location = interviewRow.children[3].textContent.trim();
	const selectionStatus = interviewRow.children[5].textContent.trim();
	const jobTitle = interviewRow.children[4].textContent.trim(); // 職種を取得

	// 企業情報行を取得する
	const companyCollapseRow = interviewRow.closest(".collapse");
	let companyName = "";
	let contactPerson = "";
	let website = "";

	if (companyCollapseRow) {
		const strongElement = companyCollapseRow.querySelector("strong");
		if (strongElement) {
			companyName = strongElement.textContent.replace("面接情報: ", "").trim();
		}

		// 隠しテキストから担当者名とウェブサイトを取得
		contactPerson = companyCollapseRow.querySelector("span:nth-of-type(1)").textContent.trim();
		website = companyCollapseRow.querySelector("span:nth-of-type(2)").textContent.trim();
	}

	// UTC変換関数
	function convertToUTC(date, time) {
		const localDate = new Date(`${date}T${time}:00`);
		return localDate.toISOString().replace(/[-:]/g, "").slice(0, 15) + "Z";
	}

	// 開始・終了日時をUTCに変換
	const startDateTime = convertToUTC(date, startTime);
	const endDateTime = convertToUTC(date, endTime);

	// GoogleカレンダーイベントURLの作成
	const title = `${companyName} - ${selectionStatus}  ${jobTitle}`; // タイトルを修正
	const description = `担当者名: ${contactPerson}様\nウェブサイト: ${website}`; // 備考欄を設定

	// カレンダーURLの作成
	const calendarUrl = `https://www.google.com/calendar/render?action=TEMPLATE&text=${encodeURIComponent(title)}&dates=${startDateTime}/${endDateTime}&location=${encodeURIComponent(location)}&details=${encodeURIComponent(description)}`;

	// 新しいタブでGoogleカレンダーイベント追加URLを開く
	window.open(calendarUrl, "_blank");
}
function openGoogleMaps(location) {
	const url = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(location)}`;
	window.open(url, '_blank');
}

function showMotivationModal(motivation) {
	$('#motivationText').text(motivation); // モーダルに志望動機をセット
	$('#motivationModal').modal('show'); // モーダルを表示
}

