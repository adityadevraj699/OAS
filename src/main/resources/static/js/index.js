document.addEventListener("DOMContentLoaded", function () {
  const track = document.getElementById("cardTrack");
  const indicators = document.getElementById("indicators");

  const cardData = [
    { title: "Hi-Tech Laboratories", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1757704666/hitech_ucqijs.jpg" },
    { title: "Skill Development", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1757704666/skill_vfdbrf.jpg" },
    { title: "Clean Environment", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1757704668/clean_oakuln.jpg" },
    { title: "Modern Infrastructure", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1757704666/infra_suly60.jpg" }
  ];

  let visibleCards = 4;
  let index = 0;
  let cards = [];

  // ----- Responsive -----
  function updateVisibleCards() {
    const width = window.innerWidth;
    if (width < 576) visibleCards = 1;
    else if (width < 768) visibleCards = 2;
    else if (width < 992) visibleCards = 3;
    else visibleCards = 4;
  }

  // ----- Render Cards -----
  function renderCards() {
    track.innerHTML = "";
    cardData.forEach(data => appendCard(data));
    cardData.forEach(data => appendCard(data, true)); // clone for infinite
    cards = Array.from(track.querySelectorAll(".col-12.col-md-3"));
  }

  function appendCard(data, clone = false) {
    const card = document.createElement("div");
    card.className = "col-12 col-md-3";
    if (clone) card.classList.add("clone");
    card.innerHTML = `
      <div class="card mx-2">
        <div class="ratio ratio-4x3">
          <img src="${data.img}" class="card-img-top w-100 h-100 object-fit-cover" alt="${data.title}">
        </div>
        <div class="card-body text-center">
          <h5>${data.title}</h5>
        </div>
      </div>`;
    track.appendChild(card);
  }

  function getCardWidth() {
    return cards[0].getBoundingClientRect().width + 16; // margin approx
  }

  function updateSlide(transition = true) {
    track.style.transition = transition ? "transform 0.5s ease" : "none";
    track.style.transform = `translateX(${-index * getCardWidth()}px)`;
    updateIndicators();
  }

  function createIndicators() {
    indicators.innerHTML = "";
    cardData.forEach((_, i) => {
      const dot = document.createElement("button");
      dot.className = "btn btn-sm mx-1 rounded-circle";
      dot.style.width = "10px";
      dot.style.height = "10px";
      dot.style.padding = "0";
      dot.style.background = i === 0 ? "black" : "lightgray";
      dot.addEventListener("click", () => goToSlide(i));
      indicators.appendChild(dot);
    });
  }

  function updateIndicators() {
    const dots = indicators.querySelectorAll("button");
    dots.forEach((d, i) => d.style.background = i === (index % cardData.length) ? "black" : "lightgray");
  }

  // ----- Slider Controls -----
  function nextSlide() {
    index++;
    updateSlide(true); // always transition

    if (index >= cardData.length) {
      // instant reset without transition (same DOM content)
      setTimeout(() => {
        index = 0;
        updateSlide(false); // jump silently
      }, 0); // match transition duration
    }
  }


  function prevSlide() {
    if (index === 0) {
      index = cardData.length;
      updateSlide(false);
      setTimeout(() => {
        index--;
        updateSlide();
      }, 0);
    } else {
      index--;
      updateSlide();
    }
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

  window.addEventListener("resize", () => {
    updateVisibleCards();
    renderCards();
    updateSlide(false);
  });

  // ----- Initialize -----
  updateVisibleCards();
  renderCards();
  createIndicators();
  updateSlide();
});





//--------Agriculture Services Industry------------
const serviceItems = document.querySelectorAll('.service-item');
const serviceImage = document.getElementById('service-image');

serviceItems.forEach(item => {
  item.addEventListener('mouseenter', () => {
    const newImg = item.getAttribute('data-img');
    serviceImage.style.opacity = 0;       // fade out
    setTimeout(() => {
      serviceImage.src = newImg;
      serviceImage.style.opacity = 1;     // fade in
    }, 250);
  });
});

