function sendOTP() {
       // TODO: Call backend to send OTP
       document.getElementById('step1').classList.add('d-none');
       document.getElementById('step2').classList.remove('d-none');
     }

     function verifyOTP() {
       // TODO: Call backend to verify OTP
       document.getElementById('step2').classList.add('d-none');
       document.getElementById('step3').classList.remove('d-none');
     }

     function resetPassword() {
       const newPass = document.getElementById('newPassword').value;
       const confirmPass = document.getElementById('confirmNewPassword').value;

       if (newPass !== confirmPass) {
         alert('Passwords do not match!');
         return;
       }

       // TODO: Call backend to update password
       alert('Password reset successful!');
       window.location.href = '/login';
     }