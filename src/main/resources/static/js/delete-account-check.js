function validateForm() {
    let x = document.getElementById("text").value;
    let msg = document.getElementById("error");
    let textArea = document.getElementById("text");
    if (x !== "Elimina il mio account") {
        msg.style.display = "block";
        textArea.value = "";
        return false;
    }
}