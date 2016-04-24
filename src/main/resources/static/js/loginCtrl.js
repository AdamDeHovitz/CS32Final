bulkAppControllers.controller("loginCtrl", 
	function($scope, $rootScope, $state, $rootScope, $ionicViewSwitcher, 
		$ionicModal, $http) {

	$scope.loginData = {};
	$scope.registerData = {};
	$scope.forms = {};

	$ionicModal.fromTemplateUrl('register-modal.html', {
	    scope: $scope,
	    animation: 'slide-in-up'
	  }).then(function(modal) {
	    $scope.modal = modal;
	  });
	  $scope.openModal = function() {
	    $scope.modal.show();
	  };
	  $scope.closeModal = function() {
	    $scope.modal.hide();
	    $scope.registerData = {};
	  	$scope.forms.registerForm.$setPristine();
	  	$scope.forms.registerForm.$setUntouched();

	  };
	  //Cleanup the modal when we're done with it!
	  $scope.$on('$destroy', function() {
	    $scope.modal.remove();
	  });
	  // Execute action on hide modal
	  $scope.$on('modal.hidden', function() {
	    // Execute action
	  });
	  // Execute action on remove modal
	  $scope.$on('modal.removed', function() {
	    // Execute action
	});

	//TODO: resize when something touched to make sure can scroll (scrolldel.resize)

	$scope.doRegister = function() {
		console.log('Doing register', $scope.registerData);

		var curData = $scope.registerData;
		var regisData = $.param({
            json: JSON.stringify({
                firstName: curData.firstName,
                lastName: curData.lastName,
                email: curData.email,
                password: curData.password,
                image: curData.img
            })
        });

		$http({
  			method: 'POST',
  			data: regisData,
  			url: '/register'
		}).then(function successCallback(response) {
			console.log("success");
		    // this callback will be called asynchronously
		    // when the response is available
		  }, function errorCallback(response) {
		  	console.log("failure");
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		  });
	};

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