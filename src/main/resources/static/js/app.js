var bulkApp = angular.module('bulkApp', [
	"ionic",
    'ngMessages',
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
    				controller: 'eventsCtrl'
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
 		 })
 		 .state('tab.event', {
 		 	url: '/event/:eventId',
    		views: {
    			'tab-feed': {
    				templateUrl: 'templates/event.html',
    				controller: 'eventCtrl'
    			}
    		}
 		 })
        .state('tab.events-event', {
            url: '/events-event/:eventId',
            views: {
                'tab-events': {
                    templateUrl: 'templates/event.html',
                    controller: 'eventCtrl'
                }
            }
         })
 		 .state('tab.profile', {
 		 	url: '/profile/:userId',
    		views: {
    			'tab-feed': {
    				templateUrl: 'templates/profile.html',
    				controller: 'profileCtrl'
    			},
    		}
 		 })
        .state('tab.events-profile', {
            url: '/events-profile/:userId',
            views: {
                'tab-events': {
                    templateUrl: 'templates/profile.html',
                    controller: 'profileCtrl'
                },
            }
         })
 		 .state('tab.account-profile', {
 		 	url: '/account-profile/:userId',
    		views: {
    			'tab-account': {
    				templateUrl: 'templates/profile.html',
    				controller: 'profileCtrl'
    			},
    		}
 		 })
        .state('tab.reviews', {
            url: '/reviews/:userId',
            views: {
                'tab-feed': {
                    templateUrl: 'templates/reviews.html',
                    controller: 'reviewsCtrl'
                },
            }
         })
        .state('tab.events-reviews', {
            url: '/events-reviews/:userId',
            views: {
                'tab-events': {
                    templateUrl: 'templates/reviews.html',
                    controller: 'reviewsCtrl'
                },
            }
         })
         .state('tab.account-reviews', {
            url: '/account-review/:userId',
            views: {
                'tab-account': {
                    templateUrl: 'templates/reviews.html',
                    controller: 'reviewsCtrl'
                },
            }
         });


	$urlRouterProvider.otherwise('/');
});

var compareTo = function() {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {
             
            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };
 
            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
};

bulkApp.directive('equals', function() {
  return {
    restrict: 'A', // only activate on element attribute
    require: '?ngModel', // get a hold of NgModelController
    link: function(scope, elem, attrs, ngModel) {
      if(!ngModel) return; // do nothing if no ng-model

      // watch own value and re-validate on change
      scope.$watch(attrs.ngModel, function() {
        validate();
      });

      // observe the other value and re-validate on change
      attrs.$observe('equals', function (val) {
        validate();
      });

      var validate = function() {
        // values
        var val1 = ngModel.$viewValue;
        var val2 = attrs.equals;

        // set validity
        ngModel.$setValidity('equals', ! val1 || ! val2 || val1 === val2);
      };
    }
  }
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
