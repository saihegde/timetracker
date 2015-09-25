'use strict';

angular.module('timetrackerApp')
    .controller('TimesheetEntryController', function ($scope, TimesheetEntry, ParseLinks) {
        $scope.timesheetEntrys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TimesheetEntry.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.timesheetEntrys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TimesheetEntry.get({id: id}, function(result) {
                $scope.timesheetEntry = result;
                $('#deleteTimesheetEntryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TimesheetEntry.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTimesheetEntryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.timesheetEntry = {hours: null, id: null};
        };
    });
