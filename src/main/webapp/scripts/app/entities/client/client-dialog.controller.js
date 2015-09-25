'use strict';

angular.module('timetrackerApp').controller('ClientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Client', 'Project',
        function($scope, $stateParams, $modalInstance, entity, Client, Project) {

        $scope.client = entity;
        $scope.projects = Project.query();
        $scope.load = function(id) {
            Client.get({id : id}, function(result) {
                $scope.client = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:clientUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.client.id != null) {
                Client.update($scope.client, onSaveFinished);
            } else {
                Client.save($scope.client, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
