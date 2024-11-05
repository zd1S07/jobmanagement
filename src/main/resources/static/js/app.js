// 削除ボタンの処理
document.querySelectorAll(".delete-btn").forEach(button => {
	button.addEventListener("click", e => {
		// `data-href`から削除URLを取得し、削除確認ボタンに設定
		const href = e.target.getAttribute("data-href");
		document.getElementById("delete-yes").setAttribute("href", href);

		// モーダルを表示
		document.getElementById("confirm-delete-modal").style.display = "block";
	});
});

// モーダルを閉じる関数
function closeModal() {
	document.getElementById("confirm-delete-modal").style.display = "none";
}




// 面接企業情報に追加ボタンの処理
document.querySelectorAll(".add-interview-btn").forEach(button => {
	button.addEventListener("click", e => {
		const companyName = e.target.getAttribute("data-company-name"); // 会社名を取得
		const userId = e.target.getAttribute("data-user-id");
		const url = e.target.getAttribute("data-href"); // URLを取得

		// モーダルに会社名を表示
		document.getElementById("add-company-name").textContent = `${companyName}を面接企業情報に追加してよろしいですか？`;

		// 追加ボタンにhref属性を設定
		document.getElementById("add-yes").setAttribute("href", url);

		// モーダルを表示
		document.getElementById("confirm-add-modal").style.display = "block";
	});
});

// モーダルを閉じる関数
function closeAddModal() {
	document.getElementById("confirm-add-modal").style.display = "none";
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

	if (templateType === '書類送付') {
		subject = "書類送付のお知らせ";
		body = `${window.selectedCompanyName} ${window.selectedContactPerson}様\nお世話になっております。\n\n以下の書類を送付いたしますので、ご確認ください。\n\n- [書類名1]\n- [書類名2]\n\nご不明な点がございましたら、お気軽にお知らせください。\n\n何卒よろしくお願いいたします。\n\nユーザー名`;
	} else if (templateType === '日程調整') {
		subject = "日程調整のお願い";
		body = `${window.selectedCompanyName} ${window.selectedContactPerson}様\nお世話になっております。\n\n面談の日程について、以下の候補をご提案いたします。\n\n- [候補日1]\n- [候補日2]\n- [候補日3]\n\nご都合はいかがでしょうか？お手数ですが、確認いただければ幸いです。\n\n何卒よろしくお願いいたします。\n\nユーザー名`;
	} else if (templateType === '御礼') {
		subject = "御礼のメール";
		body = `${window.selectedCompanyName} ${window.selectedContactPerson}様\nお世話になっております。\n\n先日はお忙しい中お時間をいただき、誠にありがとうございました。\nおかげさまで大変有意義な時間を過ごすことができました。\n\n引き続きよろしくお願いいたします。\n\n何かご不明な点があれば、いつでもお知らせください。\n\nユーザー名`;
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

function showMotivationModal(motivation) {
	$('#motivationText').text(motivation); // モーダルに志望動機をセット
	$('#motivationModal').modal('show'); // モーダルを表示
}

$(document).ready(function() {
	$('#motivationForm').on('submit', function(event) {
		event.preventDefault(); // デフォルトのフォーム送信を防ぐ

		$('#loadingMessage').show(); // ローディングメッセージを表示

		// フォームデータを取得
		var formData = $(this).serialize();

		// AJAXリクエストを送信
		$.ajax({
			url: '/motivation/generate',
			type: 'GET',
			data: formData,
			success: function(response) {
				// 生成された志望動機を表示
				$('#motivationText').text(response); // 応答データを表示

				// 志望動機が空でない場合にコピーボタンを表示
				if (response) {
					$('#copyButton').show(); // コピー機能ボタンを表示
				} else {
					$('#copyButton').hide(); // 志望動機が空の場合はボタンを非表示
				}
			},
			error: function() {
				$('#motivationText').text('エラーが発生しました。再試行してください。'); // エラーメッセージを表示
				$('#copyButton').hide(); // エラー時はコピー機能ボタンを非表示
			},
			complete: function() {
				$('#loadingMessage').hide(); // ローディングメッセージを非表示
			}
		});
	});

	// コピーボタンのクリックイベント
	$('#copyButton').on('click', function() {
		// コピー対象のテキストを取得
		const motivationText = $('#motivationText').text();

		// テキストをクリップボードにコピー
		navigator.clipboard.writeText(motivationText).then(function() {
			// コピー成功時の処理
			$('#copyFeedback').show().fadeOut(2000); // メッセージを表示して2秒後にフェードアウト
		}, function(err) {
			console.error('コピーに失敗しました:', err);
		});
	});
});