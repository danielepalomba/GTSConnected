$(document).ready(function () {
    $('#roleSelector').change(function () {
        let role = $(this).val();
        let hasBikerTrips = $('#bikerTrips').children().length > 0;
        let hasPassengerTrips = $('#passengerTrips').children().length > 0;

        if (role === 'biker') {
            $('#passengerTrips').hide();
            if (hasBikerTrips) {
                $('#bikerTrips').show();
                $('#noTripsMessage').hide();
            } else {
                $('#bikerTrips').hide();
                $('#noTripsMessage').show();
            }
        } else if (role === 'passenger') {
            $('#bikerTrips').hide();
            if (hasPassengerTrips) {
                $('#passengerTrips').show();
                $('#noTripsMessage').hide();
            } else {
                $('#passengerTrips').hide();
                $('#noTripsMessage').show();
            }
        } else {
            if (hasBikerTrips || hasPassengerTrips) {
                $('#bikerTrips').show();
                $('#passengerTrips').show();
                $('#noTripsMessage').hide();
            } else {
                $('#bikerTrips').hide();
                $('#passengerTrips').hide();
                $('#noTripsMessage').show();
            }
        }
    }).change();
});