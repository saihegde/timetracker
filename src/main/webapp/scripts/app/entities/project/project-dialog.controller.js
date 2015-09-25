'use strict';

angular.module('timetrackerApp').controller('ProjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Project', 'Client', 'Task',
        function($scope, $stateParams, $modalInstance, entity, Project, Client, Task) {

        $scope.project = entity;
        $scope.clients = Client.query();
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            Project.get({id : id}, function(result) {
                $scope.project = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:projectUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.project.id != null) {
                Project.update($scope.project, onSaveFinished);
            } else {
                Project.save($scope.project, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
