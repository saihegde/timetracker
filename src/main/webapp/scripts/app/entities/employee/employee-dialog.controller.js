'use strict';

angular.module('timetrackerApp').controller('EmployeeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Employee', 'Task', 'Timesheet',
        function($scope, $stateParams, $modalInstance, entity, Employee, Task, Timesheet) {

        $scope.employee = entity;
        $scope.tasks = Task.query();
        $scope.timesheets = Timesheet.query();
        $scope.load = function(id) {
            Employee.get({id : id}, function(result) {
                $scope.employee = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:employeeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.employee.id != null) {
                Employee.update($scope.employee, onSaveFinished);
            } else {
                Employee.save($scope.employee, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
