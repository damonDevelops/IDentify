let submissionURL = "http://localhost:8080/auth/verify";

document.getElementById('main-form').addEventListener("submit", (e) => {
    e.preventDefault();
    document.getElementById("main-submit").disabled = true;
    let spinner = document.createElement('span')
    spinner.classList.add("spinner-border")
    spinner.classList.add("spinner-border-sm")
    spinner.role = "status"
    document.getElementById("main-submit").prepend(spinner);
    submitFormData(spinner).then(r => {
        spinner.remove();
        document.getElementById("main-submit").disabled = false;
    });
});

function hideTokenIfPresent() {
    let form = new FormData(document.getElementById("main-form"));
    if (form.get('token').length !== 0) {
        document.getElementById("v-token").hidden = true;
        document.getElementById("v-email").hidden = true;
    }
}

async function submitFormData(spinner) {
    let validate = (() => {
        let result = false;
        Object.values(validatePassword()).forEach(((value) => {
            result = value;
        }));
        return result;
    })();
    if (!validate) {
        return false;
    }
    let form = new FormData(document.getElementById("main-form"));

    let data = {
        password: form.get('password'),
        email: form.get('email'),
        verificationToken: form.get('token')
    }

    let response = await fetch(submissionURL, {
        method: "POST",
        cache: "no-cache",
        headers: {
            "Content-Type": "application/json"
        },
        referrerPolicy: "no-referrer",
        body: JSON.stringify(data)
    })
    if (response.status === 200) {
        console.log("Signup complete!");
        window.location.replace("https://identify.rodeo/");
    } else {
        let body = await response.text()
        console.log(body)
    }
}

function onPasswordUpdate() {
    let form = new FormData(document.getElementById("main-form"));
    let result = validatePassword(form.get("password"));

    if (!result.passwordsMatch) {
        document.getElementById("m1").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m1").classList.replace("text-bg-danger", "text-bg-success");
    }

    if (!result.specialChars) {
        document.getElementById("m2").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m2").classList.replace("text-bg-danger", "text-bg-success");
    }

    if (!result.minNumbers) {
        document.getElementById("m3").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m3").classList.replace("text-bg-danger", "text-bg-success");
    }

    if (!result.minChars) {
        document.getElementById("m4").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m4").classList.replace("text-bg-danger", "text-bg-success");
    }

    if (!result.maxChars) {
        document.getElementById("m5").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m5").classList.replace("text-bg-danger", "text-bg-success");
    }

    if(!result.mixedCase){
        document.getElementById("m6").classList.replace("text-bg-success", "text-bg-danger");
    } else {
        document.getElementById("m6").classList.replace("text-bg-danger", "text-bg-success");
    }
}

async function removeAlertDelay(alert) {
    await new Promise(r => setTimeout(r, 5000));
    alert.remove();
}

function validatePassword() {
    let result = {
        hasContent: false,
        passwordsMatch: false,
        specialChars: false,
        minNumbers: false,
        minChars: false,
        maxChars: false,
        mixedCase: false
    }

    let form = new FormData(document.getElementById("main-form"));
    let password = form.get('password')

    result.hasContent = (() => {
        return password.length > 0;
    })();

    if (!result.hasContent) {
        return result;
    }

    result.passwordsMatch = (() => {
        return form.get('password') === form.get('passwordVerify')
    })();

    result.specialChars = (() => {
        const specialChars = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
        return specialChars.test(password.toString());
    })();

    result.minNumbers = (() => {
        return (password.match(/\d/g)?.length || 0) > 3;
    })();

    result.minChars = (() => {
        return password.length >= 8;
    })();

    result.maxChars = (() => {
        return password.length <= 30;
    })();

    result.mixedCase = (() => {
        return (/[A-Z]/.test(password.toString())) && (/[a-z]/.test(password.toString()))
    })();

    return result;
}
