document.addEventListener("DOMContentLoaded", function () {
  const serverMessage = document.getElementById("serverMessage")?.value;

  if (serverMessage && serverMessage.trim() !== "") {
    // Lowercase version for easy keyword matching
    const lowerMsg = serverMessage.toLowerCase();

    // Check for keywords that should be treated as errors
    const isError = lowerMsg.includes('invalid') || 
                    lowerMsg.includes('disabled') || 
                    lowerMsg.includes('not found');

    Swal.fire({
      title: 'Notification',
      text: serverMessage,
      icon: isError ? 'error' : 'success',
      confirmButtonColor: '#2575fc',
      background: '#f9f9f9',
      color: '#333',
      timer: 4000,
      timerProgressBar: true
    });
  }
});
