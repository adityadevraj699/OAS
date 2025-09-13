document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('contactForm');
  if (!form) return; // Skip if not on Contact page

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // -------- Validation --------
    const name = form.name.value.trim();
    const gender = form.querySelector('input[name="gender"]:checked');
    const contactNo = form.contactNo.value.trim();
    const email = form.email.value.trim();
    const subject = form.subject.value.trim();
    const message = form.message.value.trim();

    if (!name)      return alert('Name is required.');
    if (!gender)    return alert('Please select gender.');
    if (!/^[0-9]{10}$/.test(contactNo))
                    return alert('Enter a valid 10-digit contact number.');
    if (!/\S+@\S+\.\S+/.test(email))
                    return alert('Enter a valid email.');
    if (!subject)   return alert('Subject is required.');
    if (!message)   return alert('Message is required.');

    // -------- Submit via fetch --------
    const data = new URLSearchParams(new FormData(form));

    try {
      const res = await fetch(form.action, {
        method: 'POST',
        body: data
      });

      if (!res.ok) throw new Error('Server error');

      const result = await res.json();
      // Show alert first
      alert(result.message || 'Form submitted successfully!');
      // Redirect after alert confirmation
      window.location.href = '/ContactUs';
    } catch (err) {
      console.error(err);
      alert('Something went wrong. Please try again.');
    }
  });
});
