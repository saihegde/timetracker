'use strict';

angular.module('timetrackerApp')
    .controller('TimesheetController', function ($scope, Timesheet, ParseLinks) {
        $scope.timesheets = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Timesheet.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.timesheets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Timesheet.get({id: id}, function(result) {
                $scope.timesheet = result;
                $('#deleteTimesheetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Timesheet.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTimesheetConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.timesheet = {status: null, id: null};
        };
    });
