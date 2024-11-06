// DOMが完全に読み込まれた後に実行する処理
document.addEventListener('DOMContentLoaded', function () {
    loadTodoList();  // ページ読み込み時にTODOリストを表示
});

// TODOリストをローカルストレージから読み込み、表示する関数
function loadTodoList() {
    const todoList = JSON.parse(localStorage.getItem('todoList')) || [];
    const todoListElement = document.getElementById('todoList');
    todoListElement.innerHTML = ''; // 既存のリストを削除

    if (todoList.length > 0) {
        todoList.forEach(function (todo, index) {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between align-items-center';
            li.innerHTML = `
                <input type="checkbox" ${todo.completed ? 'checked' : ''} onclick="toggleComplete(${index})">
                <span style="text-decoration: ${todo.completed ? 'line-through' : 'none'}">${todo.text}<br>(期限: ${formatDeadline(todo.deadline)})</span>
                <button class="btn btn-danger btn-sm" onclick="deleteTodo(${index})">削除</button>
            `;
            todoListElement.appendChild(li);
        });
    } else {
        todoListElement.innerHTML = '<li class="list-group-item">TODOリストは空です。</li>';
    }
}

// 期限の日付をフォーマットする関数
function formatDeadline(deadline) {
    const date = new Date(deadline);
    
    // 月、日、曜日、時間、分を取得
    const month = date.getMonth() + 1; // 月は0始まりなので+1
    const day = date.getDate();
    const hour = date.getHours().toString().padStart(2, '0'); // 2桁にする
    const minute = date.getMinutes().toString().padStart(2, '0'); // 2桁にする
    const weekDays = ['日', '月', '火', '水', '木', '金', '土'];
    const weekDay = weekDays[date.getDay()];

    return `${month}月${day}日(${weekDay})${hour}:${minute}`;
}

// TODOリストの追加
document.getElementById('todoForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const todoText = document.getElementById('todoText').value;
    const todoDeadline = document.getElementById('todoDeadline').value;

    if (todoText && todoDeadline) {
        const todo = {
            text: todoText,
            deadline: todoDeadline,
            completed: false
        };

        const todoList = JSON.parse(localStorage.getItem('todoList')) || [];
        todoList.push(todo);  // 新しいTODOを追加

        localStorage.setItem('todoList', JSON.stringify(todoList));

        document.getElementById('todoText').value = '';  // 入力フォームをリセット
        document.getElementById('todoDeadline').value = '';

        loadTodoList();  // リストを再度表示
    }
});

// TODOリストの完了状態をトグル
function toggleComplete(index) {
    const todoList = JSON.parse(localStorage.getItem('todoList')) || [];
    todoList[index].completed = !todoList[index].completed;
    localStorage.setItem('todoList', JSON.stringify(todoList));
    loadTodoList();
}

// TODOリストの削除
function deleteTodo(index) {
    const todoList = JSON.parse(localStorage.getItem('todoList')) || [];
    todoList.splice(index, 1);  // 指定したインデックスのTODOを削除
    localStorage.setItem('todoList', JSON.stringify(todoList));
    loadTodoList();
}

// 削除ボタンの処理
document.querySelectorAll(".delete-btn").forEach(button => {
    button.addEventListener("click", e => {
        const href = e.target.getAttribute("data-href");
        
        // モーダルの"はい"ボタンにクリックイベントを追加
        document.getElementById("delete-yes").onclick = function () {
            window.location.href = href; // 削除処理
        };

        // モーダルを表示
        const modal = new bootstrap.Modal(document.getElementById("confirm-delete-modal"));
        modal.show();
    });
});

// 面接企業情報に追加ボタンの処理
document.querySelectorAll(".add-interview-btn").forEach(button => {
    button.addEventListener("click", e => {
        const companyName = e.target.getAttribute("data-company-name");
        const url = e.target.getAttribute("data-href");

        // モーダルに会社名を表示
        document.getElementById("add-company-name").textContent = `${companyName}を面接企業情報に追加してよろしいですか？`;

        // モーダルの"はい"ボタンにクリックイベントを追加
        document.getElementById("add-yes").onclick = function () {
            window.location.href = url; // 追加処理
        };

        // モーダルを表示
        const modal = new bootstrap.Modal(document.getElementById("confirm-add-modal"));
        modal.show();
    });
});


