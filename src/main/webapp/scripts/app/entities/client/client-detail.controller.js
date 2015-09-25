'use strict';

angular.module('timetrackerApp')
    .controller('ClientDetailController', function ($scope, $rootScope, $stateParams, entity, Client, Project) {
        $scope.client = entity;
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
            });
        };
        $rootScope.$on('timetrackerApp:clientUpdate', function(event, result) {
            $scope.client = result;
        });
    });
