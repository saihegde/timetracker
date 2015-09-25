'use strict';

angular.module('timetrackerApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Task', 'Project', 'Employee', 'TimesheetEntry',
        function($scope, $stateParams, $modalInstance, entity, Task, Project, Employee, TimesheetEntry) {

        $scope.task = entity;
        $scope.projects = Project.query();
        $scope.employees = Employee.query();
        $scope.timesheetentrys = TimesheetEntry.query();
        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:taskUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.task.id != null) {
                Task.update($scope.task, onSaveFinished);
            } else {
                Task.save($scope.task, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
