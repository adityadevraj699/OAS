document.getElementById("registerForm").addEventListener("submit", function(e) {
        e.preventDefault();

        let name = document.getElementById("name").value.trim();
        let email = document.getElementById("email").value.trim();
        let mobile = document.getElementById("mobile").value.trim();
        let username = document.getElementById("username").value.trim();
        let password = document.getElementById("password").value.trim();
        let confirmPassword = document.getElementById("confirmPassword").value.trim();

        if (password !== confirmPassword) {
          alert("Passwords do not match!");
          return;
        }

        if (!/^[0-9]{10}$/.test(mobile)) {
          alert("Mobile number must be 10 digits.");
          return;
        }

        // ✅ Form submit success (backend ke liye call karna hoga)
        alert("Registration Successful!");
        this.submit();
      });