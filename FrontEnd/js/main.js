const nav = document.querySelector(".nav")
const hamburger = document.querySelector(".more-btn")


const handleHamburgerClicked = ()=> {
    if(nav.style.display == 'none'){
        nav.style.display = "block"
    } else {
        nav.style.display = 'none'
    }
}


hamburger.addEventListener('click', handleHamburgerClicked)
