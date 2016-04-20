var bulkApp = angular.module('bulkApp', [
	"ionic",
	"bulkApp.controllers"
	]);

bulkApp.config(function($stateProvider, $urlRouterProvider) {
	
	$stateProvider
	
		.state('login', {
			url: "/",
			templateUrl: 'templates/login.html'
		})
		.state('tab', {
    		url: '/tab',
    		abstract: true,
    		templateUrl: 'templates/tabs.html'
 		 })	
 		 .state('tab.feed', {
    		url: '/home',
    		views: {
    			'tab-feed': {
    				templateUrl: 'templates/feed.html',
    				controller: 'feedCtrl'
    			}
    		}
 		 })	
 		 .state('tab.events', {
    		url: '/events',
    		views: {
    			'tab-events': {
    				templateUrl: 'templates/events.html',
    				controller: 'eventCtrl'
    			}
    		}
 		 })	
 		 .state('tab.account', {
 		 	url: '/account',
    		views: {
    			'tab-account': {
    				templateUrl: 'templates/account.html',
    				controller: 'accountCtrl'
    			}
    		}
 		 })
 		 .state('tab.joined-events', {
 		 	url: '/joined-events',
    		views: {
    			'tab-events': {
    				templateUrl: 'templates/feed.html',
    				controller: 'joinedEventsCtrl'
    			}
    		}
 		 })
 		 .state('tab.pending-events', {
 		 	url: '/pending-events',
    		views: {
    			'tab-events': {
    				templateUrl: 'templates/feed.html',
    				controller: 'pendingEventsCtrl'
    			}
    		}
 		 })
 		 .state('tab.my-events', {
 		 	url: '/my-events',
    		views: {
    			'tab-events': {
    				templateUrl: 'templates/feed.html',
    				controller: 'myEventsCtrl'
    			}
    		}
 		 });


	$urlRouterProvider.otherwise('/');
});

/*
bulkApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/",
		{
			controller: "loginCtrl",
			templateUrl: "templates/login.html"
		})
		.when("/home",
		{
			controller: "feedCtrl",
			templateUrl: "templates/feed.html"
		})
		.when("/event/:eventId",
		{
			controller: "eventCtrl",
			templateUrl: "templates/event.html"
		})
		.when("/user/:userId",
		{
			controller: "userCtrl",
			templateUrl: "templates/profile.html"
		})
		.when("/settings",
		{
			controller: "settingsCtrl",
			templateUrl: "templates/settings.html"
		})
		.otherwise({ redirectTo: "/" });

	}
]);*/

var bulkAppControllers = angular.module("bulkApp.controllers", []);
