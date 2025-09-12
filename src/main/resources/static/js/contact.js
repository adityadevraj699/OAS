<!-- Contact Form -->
<form id="contactForm" th:action="@{/submit-enquiry}" method="post">
  <!-- name, gender, contactNo, email, subject, message fields as before -->
</form>

<!-- Inline JS to handle flash messages -->
<script th:inline="javascript">
/*<![CDATA[*/
const successMsg = /*[[${success}]]*/ '' ;
const errorMsg = /*[[${error}]]*/ '' ;

document.addEventListener('DOMContentLoaded', () => {

  // ✅ Function to show alert dynamically
  function showAlert(message, type = 'success') {
    const existingAlert = document.querySelector('.custom-alert');
    if (existingAlert) existingAlert.remove();

    const alertDiv = document.createElement('div');
    alertDiv.className = 'custom-alert';
    alertDiv.textContent = message;

    alertDiv.style.position = 'fixed';
    alertDiv.style.top = '20px';
    alertDiv.style.right = '20px';
    alertDiv.style.padding = '15px 25px';
    alertDiv.style.borderRadius = '8px';
    alertDiv.style.color = '#fff';
    alertDiv.style.fontWeight = '600';
    alertDiv.style.zIndex = '9999';
    alertDiv.style.boxShadow = '0 4px 15px rgba(0,0,0,0.3)';
    alertDiv.style.opacity = '1';
    alertDiv.style.transition = 'opacity 0.5s ease';

    if (type === 'success') alertDiv.style.backgroundColor = '#28a745';
    else if (type === 'error') alertDiv.style.backgroundColor = '#dc3545';
    else alertDiv.style.backgroundColor = '#17a2b8';

    document.body.appendChild(alertDiv);

    setTimeout(() => {
      alertDiv.style.opacity = '0';
      setTimeout(() => alertDiv.remove(), 500);
    }, 3000); // 3 seconds visible
  }

  // ✅ Show flash messages from server if available
  if (successMsg) showAlert(successMsg, 'success');
  console.log(success);
  if (errorMsg) showAlert(errorMsg, 'error');

  // ✅ Form validation
  const form = document.getElementById('contactForm');
  form.addEventListener('submit', function (e) {
    let valid = true;

    const name = document.getElementById('name').value.trim();
    const gender = document.querySelector('input[name="gender"]:checked');
    const contactNo = document.getElementById('contactNo').value.trim();
    const email = document.getElementById('email').value.trim();
    const subject = document.getElementById('subject').value.trim();
    const message = document.getElementById('message').value.trim();

    if (!name) { showAlert('Name is required', 'error'); valid = false; }
    if (!gender) { showAlert('Please select your gender', 'error'); valid = false; }
    if (!/^[0-9]{10}$/.test(contactNo)) { showAlert('Enter a valid 10-digit contact number', 'error'); valid = false; }
    if (!/\S+@\S+\.\S+/.test(email)) { showAlert('Enter a valid email', 'error'); valid = false; }
    if (!subject) { showAlert('Subject is required', 'error'); valid = false; }
    if (!message) { showAlert('Message is required', 'error'); valid = false; }

    if (!valid) e.preventDefault();
  });

});
/*]]>*/
</script>
