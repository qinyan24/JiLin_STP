const signinBtn = document.getElementById("signin");
const signupBtn = document.getElementById("signup");
const container = document.querySelector(".container");

signinBtn.addEventListener("click", () => {
    // 显示登录表单，隐藏注册表单
    container.classList.remove("right-panel-active");  // 切换至登录界面
});

signupBtn.addEventListener("click", () => {
    // 显示注册表单，隐藏登录表单
    container.classList.add("right-panel-active");  // 切换至注册界面
});
