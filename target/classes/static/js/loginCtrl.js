bulkAppControllers.controller("loginCtrl", 
	function($scope, $rootScope, $state, $rootScope, $ionicViewSwitcher, 
		$ionicModal, $http, $ionicPopup) {

	$scope.loginData = {};
	$scope.registerData = {};
	$scope.forms = {};
	
	$scope.$on('$ionicView.enter', function() {
	  	$scope.forms.registerForm.$setPristine();
	  	$scope.forms.registerForm.$setUntouched();
	});



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
		var regisData = {
                firstName: curData.firstName,
                lastName: curData.lastName,
                email: curData.email,
                password: curData.password,
                image: curData.img
        };
        console.log(regisData);

        
        $.post("/register", {
                "firstName": curData.firstName,
                "lastName": curData.lastName,
                "email": curData.email,
                "password": curData.password,
                "image": curData.img
        }, function(responseJSON) {
	 		responseObject = JSON.parse(responseJSON);
	 	});

        $scope.closeModal();

	 	/*
		//$http.post("/register", regisData).
		$http({
  			method: 'POST',
  			url: '/register',
  			contentType: 'application/json',
  			data: regisData
		}).
		
		$http.post("/register", {
                'firstName': curData.firstName,
                'lastName': curData.lastName,
                'email': curData.email,
                'password': curData.password,
                'image': curData.img
            }).*/
		/*$http({
  			method: 'POST',
  			url: '/register',
  			headers : {'Content-Type': 'application/x-www-form-urlencoded'},
  			data: regisData
		}).
		then(function successCallback(response) {
			console.log("success");
		    // this callback will be called asynchronously
		    // when the response is available
		  }, function errorCallback(response) {
		  	console.log("failure");
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		  });*/
	};

	$scope.showAlert = function() {
	   var alertPopup = $ionicPopup.alert({
	     title: 'Authentication Failed',
	     template: 'Username or password is incorrect.'
	   });

	   alertPopup.then(function(res) {
	   });
	 };

	$scope.doLogin = function() {
		console.log('Doing login', $scope.loginData);

		$.post("/login", {
                "username": $scope.loginData.email,
                "password": $scope.loginData.password,
        }, function(responseJSON) {
        	console.log(responseJSON);
        	responseObject = JSON.parse(responseJSON);
        	if (responseObject.hasError) {
        		$scope.showAlert();
        	} else {
	        	$rootScope.account = {name: responseObject.name,
	    		id: responseObject.id,
	    		joined: responseObject.date,
	    		rating: 5,
	    		img: responseObject.picture,
	    		reviews: responseObject.reviews};
	    		$rootScope.authenticated = true;
	    		$ionicViewSwitcher.nextDirection('forward');
	    		$state.go("tab.feed");
	 		};
	 		$scope.loginData = {};
		  	$scope.forms.loginForm.$setPristine();
		  	$scope.forms.loginForm.$setUntouched();
	 	});
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