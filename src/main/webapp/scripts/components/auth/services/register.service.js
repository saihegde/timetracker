'use strict';

angular.module('timetrackerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


