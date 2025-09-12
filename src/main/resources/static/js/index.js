document.addEventListener("DOMContentLoaded", function () {
      const track = document.getElementById("cardTrack");
      const cards = track.children;
      const totalCards = cards.length;
      const visibleCards = 4;
      let index = 0;
      const indicators = document.getElementById("indicators");

      function getCardWidth() {
        return (cards[0].offsetWidth || 250) + 16;
      }

      for (let i = 0; i <= totalCards - visibleCards; i++) {
        const dot = document.createElement("button");
        dot.className = "btn btn-sm mx-1 rounded-circle";
        dot.style.width = "10px";
        dot.style.height = "10px";
        dot.style.padding = "0";
        dot.style.background = i === 0 ? "black" : "lightgray";
        dot.addEventListener("click", () => goToSlide(i));
        indicators.appendChild(dot);
      }
      const dots = indicators.querySelectorAll("button");

      function updateSlide() {
        track.style.transform = `translateX(${-index * getCardWidth()}px)`;
        dots.forEach((d, i) => d.style.background = i === index ? "black" : "lightgray");
      }

      function nextSlide() {
        index = (index + 1) % (totalCards - visibleCards + 1);
        updateSlide();
      }

      function prevSlide() {
        index = (index - 1 + (totalCards - visibleCards + 1)) % (totalCards - visibleCards + 1);
        updateSlide();
      }

      function goToSlide(i) {
        index = i;
        updateSlide();
      }

      document.getElementById("nextBtn").addEventListener("click", nextSlide);
      document.getElementById("prevBtn").addEventListener("click", prevSlide);

      let autoSlide = setInterval(nextSlide, 3000);
      track.addEventListener("mouseenter", () => clearInterval(autoSlide));
      track.addEventListener("mouseleave", () => autoSlide = setInterval(nextSlide, 3000));

      window.addEventListener("resize", updateSlide);
      updateSlide();
    });