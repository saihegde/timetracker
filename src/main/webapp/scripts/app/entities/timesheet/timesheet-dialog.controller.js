'use strict';

angular.module('timetrackerApp').controller('TimesheetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Timesheet', 'Employee', 'TimesheetEntry',
        function($scope, $stateParams, $modalInstance, entity, Timesheet, Employee, TimesheetEntry) {

        $scope.timesheet = entity;
        $scope.employees = Employee.query();
        $scope.timesheetentrys = TimesheetEntry.query();
        $scope.load = function(id) {
            Timesheet.get({id : id}, function(result) {
                $scope.timesheet = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:timesheetUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.timesheet.id != null) {
                Timesheet.update($scope.timesheet, onSaveFinished);
            } else {
                Timesheet.save($scope.timesheet, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
