// Mobile Menu Toggle
document.addEventListener("DOMContentLoaded", function () {
    const menuToggle = document.querySelector(".menu-toggle");
    const navbar = document.querySelector(".navbar");
    const header = document.querySelector(".header");
    const body = document.body;

    // Toggle mobile menu
    if (menuToggle) {
        menuToggle.addEventListener("click", function () {
            navbar.classList.toggle("active");
            menuToggle.classList.toggle("active");
            body.classList.toggle("menu-open");
        });
    }

    // Close menu when clicking on nav links
    const navLinks = document.querySelectorAll(".navbar a");
    navLinks.forEach((link) => {
        link.addEventListener("click", () => {
            navbar.classList.remove("active");
            menuToggle.classList.remove("active");
            body.classList.remove("menu-open");
        });
    });

    // Close menu when clicking outside
    document.addEventListener("click", function (e) {
        if (!header.contains(e.target) && navbar.classList.contains("active")) {
            navbar.classList.remove("active");
            menuToggle.classList.remove("active");
            body.classList.remove("menu-open");
        }
    });

    // Handle window resize
    window.addEventListener("resize", function () {
        if (window.innerWidth > 768) {
            navbar.classList.remove("active");
            menuToggle.classList.remove("active");
            body.classList.remove("menu-open");
        }
    });

    // Smooth scrolling for navigation links
    navLinks.forEach((link) => {
        link.addEventListener("click", function (e) {
            const href = this.getAttribute("href");
            if (href.startsWith("#")) {
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    const headerHeight = header.offsetHeight;
                    const targetPosition = target.offsetTop - headerHeight;

                    window.scrollTo({
                        top: targetPosition,
                        behavior: "smooth",
                    });
                }
            }
        });
    });

    // Search functionality
    const searchBtn = document.querySelector(".search-btn");
    const searchInput = document.querySelector(".search-input-wrapper input");

    if (searchBtn) {
        searchBtn.addEventListener("click", function () {
            const searchTerm = searchInput.value.trim();
            if (searchTerm) {
                console.log("Searching for:", searchTerm);
                // Add actual search logic here
            }
        });
    }

    if (searchInput) {
        searchInput.addEventListener("keypress", function (e) {
            if (e.key === "Enter") {
                const searchTerm = this.value.trim();
                if (searchTerm) {
                    console.log("Searching for:", searchTerm);
                    // Add actual search logic here
                }
            }
        });
    }

    // Quick links functionality
    const quickBtns = document.querySelectorAll(".quick-btn");
    quickBtns.forEach((btn) => {
        btn.addEventListener("click", function () {
            // Remove active class from all buttons
            quickBtns.forEach((b) => b.classList.remove("active"));
            // Add active class to clicked button
            this.classList.add("active");

            // Handle different quick link actions
            const btnText = this.textContent.trim();
            switch (btnText) {
                case "Tìm Bác Sĩ":
                    console.log("Redirecting to doctor search...");
                    break;
                case "Bệnh viện hoặc phòng khám":
                    console.log("Redirecting to hospital search...");
                    break;
                case "Đặt Lịch Hẹn":
                    console.log("Redirecting to appointment booking...");
                    break;
            }
        });
    });

    // Header scroll effect - only background change, no hiding
    window.addEventListener("scroll", function () {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

        if (scrollTop > 100) {
            header.classList.add("scrolled");
        } else {
            header.classList.remove("scrolled");
        }
    });

    // Animate elements on scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: "0px 0px -50px 0px",
    };

    const observer = new IntersectionObserver(function (entries) {
        entries.forEach((entry) => {
            if (entry.isIntersecting) {
                entry.target.classList.add("animate-in");
            }
        });
    }, observerOptions);

    // Observe elements for animation
    const animateElements = document.querySelectorAll(
        ".feature-item, .service-item, .department-item, .doctor-card"
    );
    animateElements.forEach((el) => {
        observer.observe(el);
    });

    // Button animations and interactions
    const buttons = document.querySelectorAll(
        ".btn-appointment, .btn-login, .btn-read-more, .btn-hero-call"
    );

    buttons.forEach((btn) => {
        btn.addEventListener("click", function (e) {
            // Add ripple effect
            const ripple = document.createElement("span");
            ripple.classList.add("ripple");
            this.appendChild(ripple);

            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;

            ripple.style.width = ripple.style.height = size + "px";
            ripple.style.left = x + "px";
            ripple.style.top = y + "px";

            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });

    // Doctor card hover effects for touch devices
    const doctorCards = document.querySelectorAll(".doctor-card");
    doctorCards.forEach((card) => {
        card.addEventListener("touchstart", function () {
            this.classList.add("touch-active");
        });

        card.addEventListener("touchend", function () {
            setTimeout(() => {
                this.classList.remove("touch-active");
            }, 300);
        });
    });

    // Scroll to top button functionality
    const scrollToTopBtn = document.getElementById("scrollToTop");

    // Show/hide scroll to top button
    window.addEventListener("scroll", function () {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

        if (scrollTop > 300) {
            scrollToTopBtn.classList.add("show");
        } else {
            scrollToTopBtn.classList.remove("show");
        }
    });

    // Scroll to top when button is clicked
    scrollToTopBtn.addEventListener("click", function () {
        smoothScrollToTop();
    });

    // Smooth scroll to top function with custom animation
    function smoothScrollToTop() {
        const currentScroll =
            window.pageYOffset || document.documentElement.scrollTop;

        if (currentScroll > 0) {
            // Calculate duration based on scroll distance (not too slow, not too fast)
            const duration = Math.min(Math.max(currentScroll / 3, 300), 800);
            const startTime = performance.now();

            function scrollAnimation(currentTime) {
                const timeElapsed = currentTime - startTime;
                const progress = Math.min(timeElapsed / duration, 1);

                // Easing function for smooth animation (ease-out-cubic)
                const easeProgress = 1 - Math.pow(1 - progress, 3);

                const scrollPosition = currentScroll * (1 - easeProgress);
                window.scrollTo(0, scrollPosition);

                if (progress < 1) {
                    requestAnimationFrame(scrollAnimation);
                }
            }

            requestAnimationFrame(scrollAnimation);
        }
    }

    // Touch events for mobile swipe
    let touchStartX = 0;
    let touchEndX = 0;

    document.addEventListener("touchstart", function (e) {
        touchStartX = e.changedTouches[0].screenX;
    });

    document.addEventListener("touchend", function (e) {
        touchEndX = e.changedTouches[0].screenX;
        handleSwipe();
    });

    function handleSwipe() {
        const swipeThreshold = 50;
        const swipeDistance = touchEndX - touchStartX;

        if (Math.abs(swipeDistance) > swipeThreshold) {
            if (swipeDistance > 0 && navbar.classList.contains("active")) {
                // Swipe right - close menu
                navbar.classList.remove("active");
                menuToggle.classList.remove("active");
                body.classList.remove("menu-open");
            }
        }
    }

    // Login Modal functionality
    const loginModal = document.getElementById("loginModal");
    const loginBtns = document.querySelectorAll(".btn-login");
    const closeLoginBtn = document.getElementById("closeLoginModal");
    const loginForm = document.getElementById("loginForm");
    const passwordInput = document.getElementById("loginPassword");

    // Show login modal when login button is clicked
    loginBtns.forEach((btn) => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            loginModal.classList.add("show");
            document.body.style.overflow = "hidden";
        });
    });

    // Close login modal functions
    function closeLoginModal() {
        loginModal.classList.remove("show");
        document.body.style.overflow = "";
        // Reset form
        loginForm.reset();
    }

    closeLoginBtn.addEventListener("click", closeLoginModal);

    // Close modal when clicking outside
    loginModal.addEventListener("click", function (e) {
        if (e.target === loginModal) {
            closeLoginModal();
        }
    });

    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            const email = document.getElementById('loginEmail').value;
            const password = document.getElementById('loginPassword').value;

            if (!email || !password) {
                e.preventDefault();
                alert('Vui lòng nhập đầy đủ email và mật khẩu!');
                return false;
            }

            // Email validation
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                e.preventDefault();
                alert('Email không hợp lệ!');
                return false;
            }

            // Show loading state
            const submitBtn = this.querySelector('.btn-login-submit');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang đăng nhập...';
            submitBtn.disabled = true;

            // Let the form submit naturally to Spring Security
            // Spring Security will handle authentication and redirect
        });
    }

    // Social login handlers
    document.querySelector(".btn-google").addEventListener("click", function () {
        console.log("Google login clicked");
        alert("Tính năng đăng nhập Google sẽ được triển khai sớm!");
    });

    document
        .querySelector(".btn-facebook")
        .addEventListener("click", function () {
            console.log("Facebook login clicked");
            alert("Tính năng đăng nhập Facebook sẽ được triển khai sớm!");
        });

    // Show signup modal (placeholder)
    document.getElementById("showSignup").addEventListener("click", function (e) {
        e.preventDefault();
        alert("Tính năng đăng ký sẽ được triển khai sớm!");
    });

    // Forgot password handler
    document
        .querySelector(".forgot-password")
        .addEventListener("click", function (e) {
            e.preventDefault();
            alert("Tính năng quên mật khẩu sẽ được triển khai sớm!");
        });

    // Appointment Modal functionality
    const appointmentModal = document.getElementById("appointmentModal");
    const appointmentBtns = document.querySelectorAll(".btn-appointment");
    const closeModalBtn = document.getElementById("closeModal");
    const cancelBtn = document.getElementById("cancelAppointment");
    const submitBtn = document.getElementById("submitAppointment");

    // Show modal when appointment button is clicked
    appointmentBtns.forEach((btn) => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();
            appointmentModal.classList.add("show");
            document.body.style.overflow = "hidden";

            // Set minimum date to today
            const today = new Date().toISOString().split("T")[0];
            document.getElementById("preferredDate").setAttribute("min", today);
        });
    });

    // Close modal functions
    function closeModal() {
        appointmentModal.classList.remove("show");
        document.body.style.overflow = "";
    }

    closeModalBtn.addEventListener("click", closeModal);
    cancelBtn.addEventListener("click", closeModal);

    // Close modal when clicking outside
    appointmentModal.addEventListener("click", function (e) {
        if (e.target === appointmentModal) {
            closeModal();
        }
    });

    // Close modal with Escape key
    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape" && appointmentModal.classList.contains("show")) {
            closeModal();
        }
    });

    // Handle form submission
    submitBtn.addEventListener("click", function (e) {
        e.preventDefault();

        // Get form data
        const formData = {
            name: document.getElementById("patientName").value,
            phone: document.getElementById("patientPhone").value,
            email: document.getElementById("patientEmail").value,
            address: document.getElementById("patientAddress").value,
            birthdate: document.getElementById("patientBirthdate").value,
            gender: document.querySelector('input[name="gender"]:checked')?.value,
            hospital: document.getElementById("hospital").value,
            department: document.getElementById("department").value,
            additionalInfo: document.getElementById("additionalInfo").value,
            preferredDate: document.getElementById("preferredDate").value,
            preferredTime: document.getElementById("preferredTime").value,
        };

        // Basic validation
        if (!formData.name || !formData.phone) {
            alert("Vui lòng điền đầy đủ họ tên và số điện thoại!");
            return;
        }

        if (!formData.hospital || !formData.department) {
            alert("Vui lòng chọn bệnh viện và chuyên khoa!");
            return;
        }

        if (!formData.preferredDate) {
            alert("Vui lòng chọn ngày khám mong muốn!");
            return;
        }

        // Simulate form submission
        console.log("Appointment Data:", formData);
        alert("Đặt lịch khám thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");

        // Reset form and close modal
        document
            .querySelectorAll(
                "#appointmentModal input, #appointmentModal select, #appointmentModal textarea"
            )
            .forEach((field) => {
                if (field.type === "radio") {
                    field.checked = field.value === "male";
                } else {
                    field.value = "";
                }
            });

        closeModal();
    });

    // Console log for debugging
    console.log("Mobile JavaScript initialized successfully!");

});

// Additional utility functions
function isMobile() {
    return window.innerWidth <= 768;
}

function isTablet() {
    return window.innerWidth > 768 && window.innerWidth <= 1024;
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Export functions for use in other scripts
window.HospitalMobile = {
    isMobile,
    isTablet,
    debounce,
};
