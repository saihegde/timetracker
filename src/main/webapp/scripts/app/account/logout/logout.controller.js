'use strict';

angular.module('timetrackerApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
