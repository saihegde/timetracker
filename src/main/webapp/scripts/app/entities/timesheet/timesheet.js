'use strict';

angular.module('timetrackerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('timesheet', {
                parent: 'entity',
                url: '/timesheets',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'timetrackerApp.timesheet.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheet/timesheets.html',
                        controller: 'TimesheetController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('timesheet');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('timesheet.detail', {
                parent: 'entity',
                url: '/timesheet/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'timetrackerApp.timesheet.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-detail.html',
                        controller: 'TimesheetDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('timesheet');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Timesheet', function($stateParams, Timesheet) {
                        return Timesheet.get({id : $stateParams.id});
                    }]
                }
            })
            .state('timesheet.new', {
                parent: 'timesheet',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-dialog.html',
                        controller: 'TimesheetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('timesheet', null, { reload: true });
                    }, function() {
                        $state.go('timesheet');
                    })
                }]
            })
            .state('timesheet.edit', {
                parent: 'timesheet',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/timesheet/timesheet-dialog.html',
                        controller: 'TimesheetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Timesheet', function(Timesheet) {
                                return Timesheet.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('timesheet', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
