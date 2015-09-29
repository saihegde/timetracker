'use strict';

angular.module('timetrackerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('timesheetEntry', {
                parent: 'entity',
                url: '/timesheetEntrys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'timetrackerApp.timesheetEntry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheetEntry/timesheetEntrys.html',
                        controller: 'TimesheetEntryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('timesheetEntry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('timesheetEntry.detail', {
                parent: 'entity',
                url: '/timesheetEntry/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'timetrackerApp.timesheetEntry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/timesheetEntry/timesheetEntry-detail.html',
                        controller: 'TimesheetEntryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('timesheetEntry');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TimesheetEntry', function($stateParams, TimesheetEntry) {
                        return TimesheetEntry.get({id : $stateParams.id});
                    }]
                }
            })
            .state('timesheetEntry.new', {
                parent: 'timesheetEntry',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/timesheetEntry/timesheetEntry-dialog.html',
                        controller: 'TimesheetEntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {hours: null, entryDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('timesheetEntry', null, { reload: true });
                    }, function() {
                        $state.go('timesheetEntry');
                    })
                }]
            })
            .state('timesheetEntry.edit', {
                parent: 'timesheetEntry',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/timesheetEntry/timesheetEntry-dialog.html',
                        controller: 'TimesheetEntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TimesheetEntry', function(TimesheetEntry) {
                                return TimesheetEntry.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('timesheetEntry', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
