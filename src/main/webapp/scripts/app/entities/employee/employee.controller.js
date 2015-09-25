'use strict';

angular.module('timetrackerApp')
    .controller('EmployeeController', function ($scope, Employee, ParseLinks) {
        $scope.employees = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Employee.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.employees = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
                $('#deleteEmployeeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEmployeeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.employee = {firstName: null, middleName: null, lastName: null, emailId: null, phoneNumber: null, id: null};
        };
    });
