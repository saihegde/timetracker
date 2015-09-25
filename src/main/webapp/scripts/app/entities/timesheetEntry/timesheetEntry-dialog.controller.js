'use strict';

angular.module('timetrackerApp').controller('TimesheetEntryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TimesheetEntry', 'Timesheet', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TimesheetEntry, Timesheet, Task) {

        $scope.timesheetEntry = entity;
        $scope.timesheets = Timesheet.query();
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TimesheetEntry.get({id : id}, function(result) {
                $scope.timesheetEntry = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('timetrackerApp:timesheetEntryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.timesheetEntry.id != null) {
                TimesheetEntry.update($scope.timesheetEntry, onSaveFinished);
            } else {
                TimesheetEntry.save($scope.timesheetEntry, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
