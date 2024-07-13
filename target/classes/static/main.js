let stompClient = null;

function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat/668bbe4b917ba34c5ab349b6', function(messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}

function showMessage(message) {
    let chatBox = document.getElementById("chatBox");
    let messageElement = document.createElement("div");
    messageElement.classList.add("message");
    messageElement.textContent = `${message.senderId}: ${message.content}`;
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function sendMessage(event) {
    event.preventDefault();
    let messageInput = document.getElementById("messageInput");
    let messageContent = messageInput.value.trim();
    if (messageContent) {
        let message = {
            chatRoomId: "668bbe4b917ba34c5ab349b6", // Replace with actual chat room ID
            senderId: "668bbb44d834f25303f35c39", // Replace with actual sender ID (if needed)
            timestamp: new Date().toISOString(),
            content: messageContent
        };

        // Make an HTTP POST request to the REST controller endpoint
        fetch('/messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        })
        .then(response => response.json())
        .then(data => {
            console.log('Message added:', data);
            // Optionally update the UI or handle the response
        })
        .catch(error => {
            console.error('Error adding message:', error);
        });

        messageInput.value = '';
    }
}

function fetchPreviousMessages() {
    let chatRoomId = "668bbe4b917ba34c5ab349b6"; // Replace with the actual chat room ID

    fetch(`/messages?chatRoomId=${chatRoomId}`)
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                showMessage(message);
            });
        })
        .catch(error => {
            console.error('Error fetching messages:', error);
        });
}

document.getElementById("messageForm").addEventListener("submit", sendMessage);

// Fetch previous messages and connect to WebSocket
fetchPreviousMessages();
connect();
