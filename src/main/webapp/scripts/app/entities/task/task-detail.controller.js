'use strict';

angular.module('timetrackerApp')
    .controller('TaskDetailController', function ($scope, $rootScope, $stateParams, entity, Task, Project, Employee, TimesheetEntry) {
        $scope.task = entity;
        $scope.load = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
            });
        };
        $rootScope.$on('timetrackerApp:taskUpdate', function(event, result) {
            $scope.task = result;
        });
    });
