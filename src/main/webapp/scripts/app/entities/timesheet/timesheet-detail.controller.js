'use strict';

angular.module('timetrackerApp')
    .controller('TimesheetDetailController', function ($scope, $rootScope, $stateParams, entity, Timesheet, Employee, TimesheetEntry) {
        $scope.timesheet = entity;
        $scope.load = function (id) {
            Timesheet.get({id: id}, function(result) {
                $scope.timesheet = result;
            });
        };
        $rootScope.$on('timetrackerApp:timesheetUpdate', function(event, result) {
            $scope.timesheet = result;
        });
    });
