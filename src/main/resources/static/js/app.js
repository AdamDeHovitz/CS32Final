var bulkApp = angular.module('bulkApp', [ "ionic", 'ngMessages',
    'monospaced.elastic', 'angularMoment', 'ngAnimate', 'toaster',
    "bulkApp.controllers", ]);

bulkApp.config(function($stateProvider, $urlRouterProvider) {

	$stateProvider

	.state('login', {
	  url : "/",
	  templateUrl : 'templates/login.html'
	}).state('tab', {
	  url : '/tab',
	  abstract : true,
	  templateUrl : 'templates/tabs.html'
	}).state('tab.feed', {
	  url : '/home',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/feed.html',
		    controller : 'feedCtrl'
		  }
	  }
	}).state('tab.events', {
	  url : '/events',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/events.html',
		    controller : 'eventsCtrl'
		  }
	  }
	}).state('tab.account', {
	  url : '/account',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/account.html',
		    controller : 'accountCtrl'
		  }
	  }
	}).state('tab.settings', {
	  url : '/settings',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/settings.html',
		    controller : 'settingsCtrl'
		  }
	  }
	}).state('tab.about', {
	  url : '/about',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/about.html',
		    controller : 'aboutCtrl'
		  }
	  }
	}).state('tab.joined-events', {
	  url : '/joined-events',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/feed.html',
		    controller : 'joinedEventsCtrl'
		  }
	  }
	}).state('tab.pending-events', {
	  url : '/pending-events',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/feed.html',
		    controller : 'pendingEventsCtrl'
		  }
	  }
	}).state('tab.my-events', {
	  url : '/my-events',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/feed.html',
		    controller : 'myEventsCtrl'
		  }
	  }
	}).state('tab.event', {
	  url : '/event/:eventId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/event.html',
		    controller : 'eventCtrl'
		  }
	  }
	}).state('tab.events-event', {
	  url : '/events-event/:eventId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/event.html',
		    controller : 'eventCtrl'
		  }
	  }
	}).state('tab.account-event', {
	  url : '/account-event/:eventId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/event.html',
		    controller : 'eventCtrl'
		  }
	  }
	}).state('tab.chat', {
	  url : '/chat/:eventId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/chat.html',
		    controller : 'chatCtrl'
		  }
	  }
	}).state('tab.events-chat', {
	  url : '/events-chat/:eventId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/chat.html',
		    controller : 'chatCtrl'
		  }
	  }
	}).state('tab.account-chat', {
	  url : '/account-chat/:eventId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/chat.html',
		    controller : 'chatCtrl'
		  }
	  }
	}).state('tab.map', {
	  url : '/map/:eventId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/map.html',
		    controller : 'mapCtrl'
		  }
	  }
	}).state('tab.events-map', {
	  url : '/events-map/:eventId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/map.html',
		    controller : 'mapCtrl'
		  }
	  }
	}).state('tab.account-map', {
	  url : '/account-map/:eventId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/map.html',
		    controller : 'mapCtrl'
		  }
	  }
	}).state('tab.requests', {
	  url : '/requests/:eventId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/requests.html',
		    controller : 'requestsCtrl'
		  }
	  }
	}).state('tab.events-requests', {
	  url : '/events-requests/:eventId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/requests.html',
		    controller : 'requestsCtrl'
		  }
	  }
	}).state('tab.account-requests', {
	  url : '/account-requests/:eventId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/requests.html',
		    controller : 'requestsCtrl'
		  }
	  }
	}).state('tab.members', {
	  url : '/members/:eventId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/members.html',
		    controller : 'membersCtrl'
		  }
	  }
	}).state('tab.events-members', {
	  url : '/events-members/:eventId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/members.html',
		    controller : 'membersCtrl'
		  }
	  }
	}).state('tab.account-members', {
	  url : '/account-members/:eventId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/members.html',
		    controller : 'membersCtrl'
		  }
	  }
	}).state('tab.profile', {
	  url : '/profile/:userId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/profile.html',
		    controller : 'profileCtrl'
		  },
	  }
	}).state('tab.events-profile', {
	  url : '/events-profile/:userId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/profile.html',
		    controller : 'profileCtrl'
		  },
	  }
	}).state('tab.account-profile', {
	  url : '/account-profile/:userId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/profile.html',
		    controller : 'profileCtrl'
		  },
	  }
	}).state('tab.reviews', {
	  url : '/reviews/:userId',
	  views : {
		  'tab-feed' : {
		    templateUrl : 'templates/reviews.html',
		    controller : 'reviewsCtrl'
		  },
	  }
	}).state('tab.events-reviews', {
	  url : '/events-reviews/:userId',
	  views : {
		  'tab-events' : {
		    templateUrl : 'templates/reviews.html',
		    controller : 'reviewsCtrl'
		  },
	  }
	}).state('tab.account-reviews', {
	  url : '/account-reviews/:userId',
	  views : {
		  'tab-account' : {
		    templateUrl : 'templates/reviews.html',
		    controller : 'reviewsCtrl'
		  },
	  }
	});

	$urlRouterProvider.otherwise('/');
});

bulkApp.run(function($rootScope, $state) {
	$rootScope.$state = $state;
	$rootScope.lat = null;
	$rootScope.lng = null;
});

var compareTo = function() {
	return {
	  require : "ngModel",
	  scope : {
		  otherModelValue : "=compareTo"
	  },
	  link : function(scope, element, attributes, ngModel) {

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
	  restrict : 'A', // only activate on element attribute
	  require : '?ngModel', // get a hold of NgModelController
	  link : function(scope, elem, attrs, ngModel) {
		  if (!ngModel)
			  return; // do nothing if no ng-model

		  // watch own value and re-validate on change
		  scope.$watch(attrs.ngModel, function() {
			  validate();
		  });

		  // observe the other value and re-validate on change
		  attrs.$observe('equals', function(val) {
			  validate();
		  });

		  var validate = function() {
			  // values
			  var val1 = ngModel.$viewValue;
			  var val2 = attrs.equals;

			  // set validity
			  ngModel.$setValidity('equals', !val1 || !val2 || val1 === val2);
		  };
	  }
	}
});

bulkApp.filter('nl2br', [ '$filter', function($filter) {
	return function(data) {
		if (!data)
			return data;
		return data.replace(/\n\r?/g, '<br />');
	};
} ]);

bulkApp.directive('autolinker', [ '$timeout', function($timeout) {
	return {
	  restrict : 'A',
	  link : function(scope, element, attrs) {
		  $timeout(function() {
			  var eleHtml = element.html();

			  if (eleHtml === '') {
				  return false;
			  }

			  var text = Autolinker.link(eleHtml, {
			    className : 'autolinker',
			    newWindow : false
			  });

			  element.html(text);

			  var autolinks = element[0].getElementsByClassName('autolinker');

			  for (var i = 0; i < autolinks.length; i++) {
				  angular.element(autolinks[i]).bind('click', function(e) {
					  var href = e.target.href;
					  console.log('autolinkClick, href: ' + href);

					  if (href) {
						  // window.open(href, '_system');
						  window.open(href, '_blank');
					  }

					  e.preventDefault();
					  return false;
				  });
			  }
		  }, 0);
	  }
	}
} ]);



function onProfilePicError(ele) {
	this.ele.src = ''; // set a fallback
}

// configure moment relative time
moment.locale('en', {
	relativeTime : {
	  future : "in %s",
	  past : "%s ago",
	  s : "a few seconds",
	  m : "a minute",
	  mm : "%d minutes",
	  h : "an hour",
	  hh : "%d hours",
	  d : "a day",
	  dd : "%d days",
	  M : "a month",
	  MM : "%d months",
	  y : "a year",
	  yy : "%d years"
	}
});

var bulkAppControllers = angular.module("bulkApp.controllers", []);
