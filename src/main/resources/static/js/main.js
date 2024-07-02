let stompClient = null;

function connect() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}

function sendMessage() {
    const messageInput = document.getElementById('message-input');
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            id: '1', // Use a unique ID in a real application
            senderId: 'user1', // Use the actual sender's ID
            receiverId: 'user2', // Use the actual receiver's ID
            content: messageContent,
            timestamp: new Date().getTime()
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function showMessage(message) {
    const chatWindow = document.getElementById('chat-window');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.textContent = message.senderId + ": " + message.content;
    chatWindow.appendChild(messageElement);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

document.addEventListener('DOMContentLoaded', function () {
    connect();
});
