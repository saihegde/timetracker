'use strict';

angular.module('timetrackerApp')
    .factory('Timesheet', function ($resource, DateUtils) {
        return $resource('api/timesheets/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
