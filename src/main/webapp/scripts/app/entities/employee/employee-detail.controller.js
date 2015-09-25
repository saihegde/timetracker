'use strict';

angular.module('timetrackerApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, Task, Timesheet) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        $rootScope.$on('timetrackerApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
    });
