'use strict';

angular.module('timetrackerApp')
    .factory('TimesheetEntry', function ($resource, DateUtils) {
        return $resource('api/timesheetEntrys/:id', {}, {
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
