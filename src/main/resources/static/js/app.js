
// Creating navbarTemplate
const navbarTemplate = document.createElement('div');
navbarTemplate.innerHTML = `
  <nav class="navbar navbar-light bg-light">
    <a class="navbar-brand" href="#">
      <img src="/images/navbar.PNG" class="d-inline-block align-top" alt="">
      Sports Buddy
    </a>
    <div id="log">
      <p>Logged in as: <a id="mainUserName">mainUser</a></p>

      <li class="nav">
        <a class="nav-link" href="/updateAccount" >Change account details</a>
      </li>
      <li class="nav">
        <a class="nav-link" href="/logout"
           onclick="return confirm('Are you sure you want to logout?')">Log out</a>
      </li>
    </div>
  </nav>
`;
//Selecting navbar position and appending new structure
const navPosition = document.querySelector('#navbar');
navPosition.appendChild(navbarTemplate);
//applying mainUserName
const mainUserName = document.querySelector('#mainUserName');
mainUserName.textContent = mainUser;
