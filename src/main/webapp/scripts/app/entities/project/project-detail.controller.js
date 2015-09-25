'use strict';

angular.module('timetrackerApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, Client, Task) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        $rootScope.$on('timetrackerApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
    });
