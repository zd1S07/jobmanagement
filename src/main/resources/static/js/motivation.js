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