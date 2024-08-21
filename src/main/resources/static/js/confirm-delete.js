document.addEventListener('DOMContentLoaded', function() {
    let deleteFormAdmin = document.getElementById('deleteFormAdmin');
    if (deleteFormAdmin) {
        deleteFormAdmin.addEventListener('submit', function (event) {
            let confirmation = confirm('Sei sicuro di voler eliminare questo viaggio?');
            if (!confirmation) {
                event.preventDefault();
            }
        });
    }
});