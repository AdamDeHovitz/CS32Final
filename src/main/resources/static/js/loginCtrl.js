bulkAppControllers.controller("loginCtrl", function($scope, $rootScope, $timeout, $state, $rootScope, $ionicViewSwitcher) {

	$scope.loginData = {};

	$scope.doLogin = function() {
		console.log('Doing login', $scope.loginData);
    	
    	$rootScope.account = {name: "Barack Obama",
    		id: 'abc123',
    		joined: 2009,
    		rating: 5,
    		img: 'http://a5.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE4MDAzNDEwNzg5ODI4MTEw.jpg',
    		reviews: []};

	    // Simulate a login delay. Remove this and replace with your login
	    // code if using a login system
    	$ionicViewSwitcher.nextDirection('forward');
	    $state.go("tab.feed");
	    
  	};
});
/*
bulkAppcontrollers.controller("loginCtrl", ['$scope', '$http', '$rootScope', '$location',
	function($scope, $http) {
		$scope.longin = function(username, password) {
			var postData = $.param({
				json: JSON.stringify({
					username: username,
					password: password
				})
			});
			$http.post("/login", postData).success(function(data, status) {
				$rootScope.userId = data.userID;
				$location.path("/home");
			});
		}

	}]);*/