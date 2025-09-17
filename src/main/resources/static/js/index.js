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

// By default second service item ki image set karna
if(serviceItems[1]) {
  serviceImage.src = serviceItems[1].getAttribute('data-img');
}

serviceItems.forEach(item => {
  item.addEventListener('mouseenter', () => {
    const newImg = item.getAttribute('data-img');
    
    // Preload image
    const img = new Image();
    img.src = newImg;
    img.onload = () => {
      serviceImage.style.opacity = 0; // fade out
      setTimeout(() => {
        serviceImage.src = newImg;
        serviceImage.style.opacity = 1; // fade in
      }, 250);
    }
  });
});

// Optional: mouseleave pe wapas default image laana
serviceItems.forEach(item => {
  item.addEventListener('mouseleave', () => {
    const defaultImg = serviceItems[1].getAttribute('data-img');
    const img = new Image();
    img.src = defaultImg;
    img.onload = () => {
      serviceImage.style.opacity = 0;
      setTimeout(() => {
        serviceImage.src = defaultImg;
        serviceImage.style.opacity = 1;
      }, 250);
    }
  });
});


//Biotech Award Winning Research Project
document.addEventListener("DOMContentLoaded", function () {
  const track1 = document.getElementById("cardTrack1");
  const indicators1 = document.getElementById("indicators1");

  const cardData1 = [
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103426/IMG_4841_awvaam.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103426/IMG_4758_zzgqs7.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103426/IMG_4773_lapyrx.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103426/IMG_4830_vylwk0.jpg" },
	{ img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103427/IMG_4822_thrjwu.jpg" },
	{ img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758103427/IMG_4799_tgoyzw.jpg" }
  ];

  let visibleCards1 = 4;
  let index1 = 0;
  let cards1 = [];

  function updateVisibleCards1() {
    const width = window.innerWidth;
    if (width < 576) visibleCards1 = 1;
    else if (width < 768) visibleCards1 = 2;
    else if (width < 992) visibleCards1 = 3;
    else visibleCards1 = 4;
  }

  function renderCards1() {
    track1.innerHTML = "";
    cardData1.forEach(data => appendCard1(data));
    cardData1.forEach(data => appendCard1(data, true));
    cards1 = Array.from(track1.querySelectorAll(".col-12.col-md-3"));
  }

  function appendCard1(data, clone = false) {
    const card = document.createElement("div");
    card.className = "col-12 col-md-3";
    if (clone) card.classList.add("clone");
    card.innerHTML = `
      <div class="card mx-2 border-0 shadow-sm">
        <div class="ratio ratio-4x3">
          <img src="${data.img}" class="card-img-top w-100 h-100 object-fit-cover rounded" alt="${data.title}">
        </div>
      </div>`;
    track1.appendChild(card);
  }

  function getCardWidth1() {
    return cards1[0].getBoundingClientRect().width + 16;
  }

  function updateSlide1(transition = true) {
    track1.style.transition = transition ? "transform 0.5s ease" : "none";
    track1.style.transform = `translateX(${-index1 * getCardWidth1()}px)`;
    updateIndicators1();
  }

  function createIndicators1() {
    indicators1.innerHTML = "";
    cardData1.forEach((_, i) => {
      const dot = document.createElement("button");
      dot.className = "btn btn-sm mx-1 rounded-circle";
      dot.style.width = "10px";
      dot.style.height = "10px";
      dot.style.padding = "0";
      dot.style.background = i === 0 ? "black" : "lightgray";
      dot.addEventListener("click", () => goToSlide1(i));
      indicators1.appendChild(dot);
    });
  }

  function updateIndicators1() {
    const dots = indicators1.querySelectorAll("button");
    dots.forEach((d, i) => d.style.background = i === (index1 % cardData1.length) ? "black" : "lightgray");
  }

  function nextSlide1() {
    index1++;
    updateSlide1(true);
    if (index1 >= cardData1.length) {
      setTimeout(() => {
        index1 = 0;
        updateSlide1(false);
      }, 0);
    }
  }

  function prevSlide1() {
    if (index1 === 0) {
      index1 = cardData1.length;
      updateSlide1(false);
      setTimeout(() => {
        index1--;
        updateSlide1();
      }, 0);
    } else {
      index1--;
      updateSlide1();
    }
  }

  function goToSlide1(i) {
    index1 = i;
    updateSlide1();
  }

  document.getElementById("nextBtn1").addEventListener("click", nextSlide1);
  document.getElementById("prevBtn1").addEventListener("click", prevSlide1);

  let autoSlide1 = setInterval(nextSlide1, 3000);
  track1.addEventListener("mouseenter", () => clearInterval(autoSlide1));
  track1.addEventListener("mouseleave", () => autoSlide1 = setInterval(nextSlide1, 3000));

  window.addEventListener("resize", () => {
    updateVisibleCards1();
    renderCards1();
    updateSlide1(false);
  });

  updateVisibleCards1();
  renderCards1();
  createIndicators1();
  updateSlide1();
});






//Training Programme For Students

document.addEventListener("DOMContentLoaded", function () {
  const track = document.getElementById("cardTrack2");
  const indicators = document.getElementById("indicators2");

  const cardData = [
    { title: "Biodiesel-Production Testing", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113893/tra1_op0mne.jpg" },
    { title: "Bioinformatics", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113894/tra2_msg16y.jpg" },
    { title: "Industrial Biotechnology", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113895/tra3_l7te0d.jpg" },
    { title: "Microbiology", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113893/tra5_iihlco.jpg" },
	{ title: "Plant Tissue Culture", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113894/tra6_wpinca.jpg" },
	{ title: "Industrial Training", img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758113893/download_ulkuww.jpg" }
  ];

  let visibleCards = 4, index = 0, cards = [];

  function updateVisibleCards() {
    const w = window.innerWidth;
    if (w < 400) visibleCards = 1;
    else if (w < 576) visibleCards = 2;
    else if (w < 768) visibleCards = 3;
    else visibleCards = 4;
  }

  function appendCard(data, clone = false) {
  const card = document.createElement("div");
  card.className = "col-12 col-sm-6 col-md-3 px-1";
  if (clone) card.classList.add("clone");

  card.innerHTML = `
    <div class="card mx-1 shadow-sm border-0">
      <!-- Image Container -->
      <div class="card-img-container d-flex align-items-center justify-content-center">
        <img src="${data.img}" class="card-img-top rounded" alt="${data.title}">
      </div>

      <!-- Text Container -->
      <div class="card-text-container text-center">
        <h6 class="mb-0">${data.title}</h6>
      </div>
    </div>`;
  track.appendChild(card);
}

  function renderCards() {
    track.innerHTML = "";
    cardData.forEach(d => appendCard(d));
    cardData.forEach(d => appendCard(d, true));
    cards = Array.from(track.querySelectorAll(".col-12"));
  }

  function getCardWidth() {
    return cards[0].getBoundingClientRect().width + 8;
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
      dot.style.cssText = "width:10px;height:10px;padding:0;background:lightgray;border:none;";
      dot.addEventListener("click", () => goToSlide(i));
      indicators.appendChild(dot);
    });
    updateIndicators();
  }

  function updateIndicators() {
    indicators.querySelectorAll("button").forEach((d, i) => {
      d.style.background = i === (index % cardData.length) ? "black" : "lightgray";
    });
  }

  function nextSlide() {
    index++;
    updateSlide(true);
    if (index >= cardData.length) {
      setTimeout(() => { index = 0; updateSlide(false); }, 500);
    }
  }

  function prevSlide() {
    if (index === 0) {
      index = cardData.length;
      updateSlide(false);
      setTimeout(() => { index--; updateSlide(true); }, 20);
    } else {
      index--;
      updateSlide(true);
    }
  }

  function goToSlide(i) {
    index = i;
    updateSlide(true);
  }

  document.getElementById("nextBtn2").addEventListener("click", nextSlide);
  document.getElementById("prevBtn2").addEventListener("click", prevSlide);

  let autoSlide = setInterval(nextSlide, 3000);
  track.addEventListener("mouseenter", () => clearInterval(autoSlide));
  track.addEventListener("mouseleave", () => autoSlide = setInterval(nextSlide, 3000));

  window.addEventListener("resize", () => {
    updateVisibleCards();
    renderCards();
    updateSlide(false);
  });

  updateVisibleCards();
  renderCards();
  createIndicators();
  updateSlide(false);
});





//Our Clients
document.addEventListener("DOMContentLoaded", function () {
  const track4 = document.getElementById("cardTrack4");
  const indicators4 = document.getElementById("indicators4");

  const cardData4 = [
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115794/cp1_brcxi1.png" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115793/cp2_tjwcpl.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115794/cp3_gxllaw.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115794/cp6_apj7dy.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115794/cp5_plg5ws.jpg" },
    { img: "https://res.cloudinary.com/ddtcj9ks5/image/upload/v1758115793/cl1_ipr8k9.png" }
  ];

  let visibleCards4 = 4; // ✅ Show 4 images on desktop
  let index4 = 0;
  let cards4 = [];

  function updateVisibleCards4() {
    const width = window.innerWidth;
    if (width < 576) visibleCards4 = 1;   // Mobile
    else if (width < 768) visibleCards4 = 2; // Small tablets
    else if (width < 992) visibleCards4 = 3; // Tablets
    else visibleCards4 = 4;               // ✅ Desktop: 4 cards
  }

  function renderCards4() {
    track4.innerHTML = "";
    cardData4.forEach(data => appendCard4(data));
    cardData4.forEach(data => appendCard4(data, true)); // clone for smooth looping
    cards4 = Array.from(track4.querySelectorAll(".col-12.col-md-3")); // ✅ Use col-md-3
  }

  function appendCard4(data, clone = false) {
  const card = document.createElement("div");
  card.className = "col-12 col-md-3"; // ✅ 4 cards on desktop
  if (clone) card.classList.add("clone");
  card.innerHTML = `
    <div class="card border-0  d-flex justify-content-center align-items-center bg-transparent" 
         style="width:90%; height:80%; ">
      <img src="${data.img}" 
           alt="client" 
           style="max-width:100%; max-height:100%; height:auto; object-fit:contain; border-radius:8px;">
    </div>`;
  track4.appendChild(card);
}


  function getCardWidth4() {
    return cards4[0].getBoundingClientRect().width + 24; // adjust gap
  }

  function updateSlide4(transition = true) {
    track4.style.transition = transition ? "transform 0.5s ease" : "none";
    track4.style.transform = `translateX(${-index4 * getCardWidth4()}px)`;
    updateIndicators4();
  }

  function createIndicators4() {
    indicators4.innerHTML = "";
    cardData4.forEach((_, i) => {
      const dot = document.createElement("button");
      dot.className = "btn btn-sm mx-1 rounded-circle";
      dot.style.width = "10px";
      dot.style.height = "10px";
      dot.style.padding = "0";
      dot.style.background = i === 0 ? "black" : "lightgray";
      dot.addEventListener("click", () => goToSlide4(i));
      indicators4.appendChild(dot);
    });
  }

  function updateIndicators4() {
    const dots = indicators4.querySelectorAll("button");
    dots.forEach((d, i) => d.style.background = i === (index4 % cardData4.length) ? "black" : "lightgray");
  }

  function nextSlide4() {
    index4++;
    updateSlide4(true);
    if (index4 >= cardData4.length) {
      setTimeout(() => {
        index4 = 0;
        updateSlide4(false);
      }, 0);
    }
  }

  function prevSlide4() {
    if (index4 === 0) {
      index4 = cardData4.length;
      updateSlide4(false);
      setTimeout(() => {
        index4--;
        updateSlide4();
      }, 0);
    } else {
      index4--;
      updateSlide4();
    }
  }

  function goToSlide4(i) {
    index4 = i;
    updateSlide4();
  }

  document.getElementById("nextBtn4").addEventListener("click", nextSlide4);
  document.getElementById("prevBtn4").addEventListener("click", prevSlide4);

  let autoSlide4 = setInterval(nextSlide4, 3000);
  track4.addEventListener("mouseenter", () => clearInterval(autoSlide4));
  track4.addEventListener("mouseleave", () => autoSlide4 = setInterval(nextSlide4, 3000));

  window.addEventListener("resize", () => {
    updateVisibleCards4();
    renderCards4();
    updateSlide4(false);
  });

  updateVisibleCards4();
  renderCards4();
  createIndicators4();
  updateSlide4();
});






