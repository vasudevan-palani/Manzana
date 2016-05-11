'use strict';
/**
 * @ngdoc overview
 * @name sbAdminApp
 * @description
 * # sbAdminApp
 *
 * Main module of the application.
 */
angular
  .module('manzana', [
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'ui.select', 
    'ngSanitize',
    'nvd3',
    'anguFixedHeaderTable'
  ])
  .config(['$stateProvider','$urlRouterProvider','$ocLazyLoadProvider',function ($stateProvider,$urlRouterProvider,$ocLazyLoadProvider) {
    
    $ocLazyLoadProvider.config({
      debug:false,
      events:true,
    });

    $urlRouterProvider.otherwise('/add');

    $stateProvider
      .state('search',{
        templateUrl:'views/pages/search.html',
        url:'/search',
        controller:'SearchController'
    })
      .state('add',{
        templateUrl:'views/pages/add.html',
        url:'/add',
        controller:'AddController'
    })
      .state('edit',{
        templateUrl:'views/pages/edit.html',
        url:'/edit',
        controller:'EditController'
    })
  }]);


