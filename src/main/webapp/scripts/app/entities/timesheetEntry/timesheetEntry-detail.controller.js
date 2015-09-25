'use strict';

angular.module('timetrackerApp')
    .controller('TimesheetEntryDetailController', function ($scope, $rootScope, $stateParams, entity, TimesheetEntry, Timesheet, Task) {
        $scope.timesheetEntry = entity;
        $scope.load = function (id) {
            TimesheetEntry.get({id: id}, function(result) {
                $scope.timesheetEntry = result;
            });
        };
        $rootScope.$on('timetrackerApp:timesheetEntryUpdate', function(event, result) {
            $scope.timesheetEntry = result;
        });
    });
